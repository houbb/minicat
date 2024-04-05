package com.github.houbb.minicat.dto;

import javax.servlet.http.HttpServletResponse;

/**
 * @since 0.4.0
 */
public interface IMiniCatResponse extends HttpServletResponse {

    /**
     * 写入结果
     * @param text 文本
     * @param charset 编码
     */
    void write(String text, String charset);


    /**
     * 写入结果
     * @param text 文本
     */
    default void write(String text) {
        this.write(text, "UTF-8");
    }

}
