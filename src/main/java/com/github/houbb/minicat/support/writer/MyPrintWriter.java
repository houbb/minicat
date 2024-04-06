package com.github.houbb.minicat.support.writer;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.minicat.util.InnerHttpUtil;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;

import java.io.*;
import java.nio.charset.Charset;

public class MyPrintWriter extends PrintWriter {

    private static final Log logger = LogFactory.getLog(MyPrintWriter.class);

    public MyPrintWriter(Writer out) {
        super(out);
    }

    public MyPrintWriter(Writer out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public MyPrintWriter(OutputStream out) {
        super(out);
    }

    public MyPrintWriter(OutputStream out, boolean autoFlush) {
        super(out, autoFlush);
    }

    public MyPrintWriter(String fileName) throws FileNotFoundException {
        super(fileName);
    }

    public MyPrintWriter(String fileName, String csn) throws FileNotFoundException, UnsupportedEncodingException {
        super(fileName, csn);
    }

    public MyPrintWriter(File file) throws FileNotFoundException {
        super(file);
    }

    public MyPrintWriter(File file, String csn) throws FileNotFoundException, UnsupportedEncodingException {
        super(file, csn);
    }

    private ChannelHandlerContext ctx;

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    public void print(String text) {
        text = InnerHttpUtil.http200Resp(text);
        Charset charset = Charset.forName("UTF-8");
        ByteBuf responseBuf = Unpooled.copiedBuffer(text, charset);
        ctx.writeAndFlush(responseBuf)
                .addListener(ChannelFutureListener.CLOSE); // Close the channel after sending the response
        logger.info("[MiniCat] channelRead writeAndFlush DONE");
    }

}
