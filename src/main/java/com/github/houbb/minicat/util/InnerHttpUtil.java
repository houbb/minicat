package com.github.houbb.minicat.util;

public class InnerHttpUtil {

    /**
     * 符合 http 标准的字符串
     * @param rawText 原始文本
     * @return 结果
     */
    public static String http200Resp(String rawText) {
        String format = "HTTP/1.1 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                "\r\n" +
                "%s";

        return String.format(format, rawText);
    }

    /**
     * 404 响应
     * @return 结果
     * @since 0.2.0
     */
    public static String http404Resp() {
        String response = "HTTP/1.1 404 Not Found\r\n" +
                "Content-Type: text/plain\r\n" +
                "\r\n" +
                "404 Not Found: The requested resource was not found on this server.";

        return response;
    }

    /**
     * 500 响应
     * @return 结果
     * @since 0.2.0
     */
    public static String http500Resp() {
        return "HTTP/1.1 500 Internal Server Error\r\n" +
                "Content-Type: text/plain\r\n" +
                "\r\n" +
                "500 Internal Server Error: The server encountered an unexpected condition that prevented it from fulfilling the request.";
    }

}
