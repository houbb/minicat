package com.github.houbb.minicat.dto;

/**
 * @since 0.4.0
 */
public abstract class MiniCatResponseCommon extends MiniCatResponseAdaptor {

    public abstract void write(String text, String charset);

}
