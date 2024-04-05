package com.github.houbb.minicat.bs;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.minicat.bo.RequestInfoBo;
import com.github.houbb.minicat.dto.IMiniCatRequest;
import com.github.houbb.minicat.dto.IMiniCatResponse;
import com.github.houbb.minicat.dto.MiniCatRequestCommon;
import com.github.houbb.minicat.dto.MiniCatResponseCommon;
import com.github.houbb.minicat.support.request.IRequestDispatcher;
import com.github.houbb.minicat.support.request.RequestDispatcherContext;
import com.github.houbb.minicat.support.request.RequestDispatcherManager;
import com.github.houbb.minicat.support.servlet.IServletManager;
import com.github.houbb.minicat.support.servlet.WebXmlServletManager;
import com.github.houbb.minicat.util.InnerHttpUtil;
import com.github.houbb.minicat.util.InnerRequestUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

class MiniCatServerHandler extends ChannelInboundHandlerAdapter {

    private static final Log logger = LogFactory.getLog(MiniCatServerHandler.class);


    /**
     * servlet 管理
     *
     * @since 0.3.0
     */
    private final IServletManager servletManager = new WebXmlServletManager();

    /**
     * 请求分发
     *
     * @since 0.3.0
     */
    private final IRequestDispatcher requestDispatcher = new RequestDispatcherManager();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf buf = (ByteBuf) msg;
        byte[] bytes = new byte[buf.readableBytes()];
        buf.readBytes(bytes);
        String requestString = new String(bytes, Charset.defaultCharset());
        logger.info("[MiniCat] channelRead requestString={}", requestString);


        // 获取请求信息
        RequestInfoBo requestInfoBo = InnerRequestUtil.buildRequestInfoBo(requestString);
        IMiniCatRequest request = new MiniCatRequestCommon(requestInfoBo.getMethod(), requestInfoBo.getUrl());
        IMiniCatResponse response = new MiniCatResponseCommon() {
            @Override
            public void write(String text, String charsetStr) {
                Charset charset = Charset.forName(charsetStr);
                ByteBuf responseBuf = Unpooled.copiedBuffer(text, charset);
                ctx.writeAndFlush(responseBuf)
                        .addListener(ChannelFutureListener.CLOSE); // Close the channel after sending the response
                logger.info("[MiniCat] channelRead writeAndFlush DONE");
            }
        };

        // 分发调用
        final RequestDispatcherContext dispatcherContext = new RequestDispatcherContext();
        dispatcherContext.setRequest(request);
        dispatcherContext.setResponse(response);
        dispatcherContext.setServletManager(servletManager);
        requestDispatcher.dispatch(dispatcherContext);
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
