package com.github.houbb.minicat.support.request;

import com.github.houbb.minicat.dto.IMiniCatRequest;
import com.github.houbb.minicat.dto.IMiniCatResponse;
import com.github.houbb.minicat.support.servlet.IServletManager;

public class RequestDispatcherContext {

    private IMiniCatRequest request;

    private IMiniCatResponse response;

    private IServletManager servletManager;

    /**
     * 基本文件夹
     * @since 0.5.0
     */
    private String baseDir;

    public IMiniCatRequest getRequest() {
        return request;
    }

    public void setRequest(IMiniCatRequest request) {
        this.request = request;
    }

    public IMiniCatResponse getResponse() {
        return response;
    }

    public void setResponse(IMiniCatResponse response) {
        this.response = response;
    }

    public IServletManager getServletManager() {
        return servletManager;
    }

    public void setServletManager(IServletManager servletManager) {
        this.servletManager = servletManager;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }
}
