package com.github.houbb.minicat.dto;

import com.github.houbb.minicat.exception.MiniCatException;

import java.io.IOException;
import java.io.OutputStream;

/**
 * @since 0.2.0
 */
public class MiniCatResponse {

    private final OutputStream outputStream;

    public MiniCatResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void write(String text, String charset) {
        try {
            outputStream.write(text.getBytes(charset));
        } catch (IOException e) {
            throw new MiniCatException(e);
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
