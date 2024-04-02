package com.github.houbb.minicat.util;

public class InnerHttpUtil {

    /**
     * 符合 http 标准的字符串
     * @param rawText 原始文本
     * @return 结果
     */
    public static String httpResp(String rawText) {
        String format = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                "\r\n" +
                "%s";

        return String.format(format, rawText);
    }

}
