package com.github.houbb.minicat.support.request;

import com.github.houbb.minicat.dto.IMiniCatRequest;
import com.github.houbb.minicat.dto.IMiniCatResponse;
import com.github.houbb.minicat.support.servlet.IServletManager;

public class RequestDispatcherContext {

    private IMiniCatRequest request;

    private IMiniCatResponse response;

    private IServletManager servletManager;

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
}
