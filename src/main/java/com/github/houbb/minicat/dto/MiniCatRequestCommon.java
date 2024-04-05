package com.github.houbb.minicat.dto;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * @since 0.2.0
 */
public class MiniCatRequestCommon extends MiniCatRequestAdaptor {

    private static final Log logger = LogFactory.getLog(MiniCatRequestCommon.class);

    /**
     * 请求方式 例如：GET/POST
     */
    private String method;


    /**
     * / ， /index.html
     */

    private String url;

    public MiniCatRequestCommon(String method, String url) {
        this.method = method;
        this.url = url;
    }

    @Override
    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    @Override
    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
