package com.github.houbb.minicat.exception;

public class MiniCatException extends RuntimeException {

    public MiniCatException() {
    }

    public MiniCatException(String message) {
        super(message);
    }

    public MiniCatException(String message, Throwable cause) {
        super(message, cause);
    }

    public MiniCatException(Throwable cause) {
        super(cause);
    }

    public MiniCatException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
