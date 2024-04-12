package com.github.houbb.minicat.bs;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.minicat.bo.RequestInfoBo;
import com.github.houbb.minicat.dto.IMiniCatRequest;
import com.github.houbb.minicat.dto.IMiniCatResponse;
import com.github.houbb.minicat.dto.MiniCatRequestCommon;
import com.github.houbb.minicat.dto.MiniCatResponseCommon;
import com.github.houbb.minicat.support.context.MiniCatContextConfig;
import com.github.houbb.minicat.support.listener.IListenerManager;
import com.github.houbb.minicat.support.request.IRequestDispatcher;
import com.github.houbb.minicat.support.writer.MyPrintWriter;
import com.github.houbb.minicat.util.InnerRequestUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import javax.servlet.ReadListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.WriteListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.List;

class MiniCatServerHandler extends ChannelInboundHandlerAdapter {

    private static final Log logger = LogFactory.getLog(MiniCatServerHandler.class);


    /**
     * 请求分发
     *
     * @since 0.3.0
     */
    private final IRequestDispatcher requestDispatcher;

    /**
     * 配置信息
     *
     * @since 0.6.0
     */
    private final MiniCatContextConfig miniCatContextConfig;

    /**
     * 监听器管理类
     *
     * @since 0.7.0
     */
    private final IListenerManager listenerManager;

    MiniCatServerHandler(final MiniCatContextConfig miniCatContextConfig) {
        this.requestDispatcher = miniCatContextConfig.getRequestDispatcher();
        this.miniCatContextConfig = miniCatContextConfig;
        this.listenerManager = miniCatContextConfig.getListenerManager();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        final List<ReadListener> readListeners = listenerManager.getReadListeners();

        String requestString = null;
        try {
            ByteBuf buf = (ByteBuf) msg;
            byte[] bytes = new byte[buf.readableBytes()];
            buf.readBytes(bytes);
            requestString = new String(bytes, Charset.defaultCharset());
            logger.info("[MiniCat] channelRead requestString={}", requestString);

            // 读取
            readListeners.forEach(readListener -> {
                try {
                    readListener.onDataAvailable();
                    readListener.onAllDataRead();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            // 读取
            readListeners.forEach(readListener -> {
                readListener.onError(e);
            });
            logger.info("[MiniCat] channelRead meet ex", e);
        }


        // 获取请求信息
        RequestInfoBo requestInfoBo = InnerRequestUtil.buildRequestInfoBo(requestString);
        IMiniCatRequest request = new MiniCatRequestCommon(requestInfoBo.getMethod(), requestInfoBo.getUrl());
        // 初始化 request
        final ServletRequestEvent servletRequestEvent = new ServletRequestEvent(miniCatContextConfig.getServletContext(), request);
        final List<ServletRequestListener> servletRequestListenerList = listenerManager.getServletRequestListeners();
        servletRequestListenerList.forEach(servletRequestListener -> servletRequestListener.requestInitialized(servletRequestEvent));

        IMiniCatResponse response = new MiniCatResponseCommon() {
            // 兼容一下 getPrint 的写法，其实实现的还是过于简陋了。
            @Override
            public PrintWriter getWriter() throws IOException {
                // 创建临时文件，文件名前缀为"temp"，后缀为".txt"，存放在默认临时目录中
                File tempFile = File.createTempFile("MiniCatTempFile", ".txt");
                // 可选：设置临时文件在JVM退出时自动删除
                tempFile.deleteOnExit();

                MyPrintWriter myPrintWriter = new MyPrintWriter(tempFile.getAbsoluteFile());
                myPrintWriter.setCtx(ctx);

                return myPrintWriter;
            }

            @Override
            public void write(String text, String charsetStr) {
                final List<WriteListener> writeListeners = listenerManager.getWriteListeners();
                try {
                    Charset charset = Charset.forName(charsetStr);
                    ByteBuf responseBuf = Unpooled.copiedBuffer(text, charset);
                    ctx.writeAndFlush(responseBuf)
                            .addListener(ChannelFutureListener.CLOSE); // Close the channel after sending the response
                    logger.info("[MiniCat] channelRead writeAndFlush DONE");

                    // 写入
                    writeListeners.forEach(writeListener -> {
                        try {
                            writeListener.onWritePossible();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
                } catch (Exception e) {
                    logger.error("[MiniCat] channel write failed", e);
                    // 写入
                    writeListeners.forEach(writeListener -> {
                        writeListener.onError(e);
                    });
                }
            }
        };

        // 分发调用
        requestDispatcher.dispatch(request, response, miniCatContextConfig);

        // 结束
        servletRequestListenerList.forEach(servletRequestListener -> servletRequestListener.requestDestroyed(servletRequestEvent));
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("exceptionCaught", cause);
        ctx.close();
    }
}
