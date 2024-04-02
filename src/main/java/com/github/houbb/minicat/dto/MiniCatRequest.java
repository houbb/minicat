package com.github.houbb.minicat.dto;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @since 0.2.0
 */
public class MiniCatRequest {

    private static final Log logger = LogFactory.getLog(MiniCatRequest.class);

    /**
     * 请求方式 例如：GET/POST
     */
    private String method;


    /**
     * / ， /index.html
     */

    private String url;


    /**
     * 其他的属性都是通过inputStream解析出来的。
     */

    private InputStream inputStream;

    public MiniCatRequest(InputStream inputStream) {
        this.inputStream = inputStream;

        this.readFromStream();
    }

    private void readFromStream() {
        try {
            //从输入流中获取请求信息
            int count = inputStream.available();
            byte[] bytes = new byte[count];
            int readResult = inputStream.read(bytes);
            String inputsStr = new String(bytes);
            logger.info("[MiniCat] readCount={}, input stream {}", readResult, inputsStr);

            //获取第一行数据
            String firstLineStr = inputsStr.split("\\n")[0];  //GET / HTTP/1.1
            String[] strings = firstLineStr.split(" ");
            this.method = strings[0];
            this.url = strings[1];
        } catch (IOException e) {
            logger.error("[MiniCat] readFromStream meet ex", e);
            throw new RuntimeException(e);
        }
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public InputStream getInputStream() {
        return inputStream;
    }

    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public String toString() {
        return "MiniCatRequest{" + "method='" + method + '\'' + ", url='" + url + '\'' + '}';
    }

}
