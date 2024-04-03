package com.github.houbb.minicat.support.request;

import com.github.houbb.minicat.dto.MiniCatRequest;
import com.github.houbb.minicat.dto.MiniCatResponse;
import com.github.houbb.minicat.support.servlet.IServletManager;

public class RequestDispatcherContext {

    private MiniCatRequest request;

    private MiniCatResponse response;

    private IServletManager servletManager;

    public MiniCatRequest getRequest() {
        return request;
    }

    public void setRequest(MiniCatRequest request) {
        this.request = request;
    }

    public MiniCatResponse getResponse() {
        return response;
    }

    public void setResponse(MiniCatResponse response) {
        this.response = response;
    }

    public IServletManager getServletManager() {
        return servletManager;
    }

    public void setServletManager(IServletManager servletManager) {
        this.servletManager = servletManager;
    }
}
