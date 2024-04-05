package com.github.houbb.minicat.dto;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.minicat.exception.MiniCatException;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * @since 0.2.0
 */
public class MiniCatResponseBio extends MiniCatResponseAdaptor {

    private static final Log logger = LogFactory.getLog(MiniCatResponseBio.class);

    private final OutputStream outputStream;

    public MiniCatResponseBio(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        // 重载输出实现
        return new PrintWriter(this.outputStream);
    }

    public void write(String text, String charset) {
        try {
            outputStream.write(text.getBytes(charset));
        } catch (IOException e) {
            logger.error("[MiniCat] write failed text={}, charset={}", text, charset, e);
//            throw new MiniCatException(e);

        }
    }

    public void write(String text) {
        write(text, "UTF-8");
    }

    public void write(byte[] bytes) {
        try {
            outputStream.write(bytes);
        } catch (IOException e) {
            throw new MiniCatException(e);
        }
    }

}
