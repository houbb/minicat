package com.github.houbb.minicat.util;

import com.github.houbb.minicat.bo.RequestInfoBo;

import java.io.IOException;
import java.io.InputStream;

public class InnerRequestUtil {

    /**
     *
     * Server read: GET /my HTTP/1.1
     * Host: localhost:8080
     * Connection: keep-alive
     * Cache-Control: max-age=0
     * sec-ch-ua: "Google Chrome";v="123", "Not:A-Brand";v="8", "Chromium";v="123"
     * sec-ch-ua-mobile: ?0
     * sec-ch-ua-platform: "Windows"
     * Upgrade-Insecure-Requests: 1
     * User-Agent: Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/123.0.0.0 Safari/537.36
     *
     * @param text 文本
     * @return 结果
     * @since 0.4.0
     */
    public static RequestInfoBo buildRequestInfoBo(String text) {
        // 使用正则表达式按行分割请求字符串
        String[] requestLines = text.split("\r\n");

        // 获取第一行请求行
        String firstLine = requestLines[0];

        String[] strings = firstLine.split(" ");
        String method = strings[0];
        String url = strings[1];

        return new RequestInfoBo(url, method);
    }

    public static RequestInfoBo buildRequestInfoBo(final InputStream inputStream) {
        byte[] buffer = new byte[1024]; // 使用固定大小的缓冲区
        int bytesRead = 0;

        try {
            while ((bytesRead = inputStream.read(buffer)) != -1) { // 循环读取数据直到EOF
                String inputStr = new String(buffer, 0, bytesRead);

                // 检查是否读取到完整的HTTP请求行
                if (inputStr.contains("\n")) {
                    // 获取第一行数据
                    String firstLineStr = inputStr.split("\\n")[0];

                    return buildRequestInfoBo(firstLineStr);
                }
            }

            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
