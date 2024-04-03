package com.github.houbb.minicat.support.servlet;

import com.github.houbb.minicat.constant.HttpMethodType;

import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AbstractMiniCatHttpServlet extends HttpServlet {

    public abstract void doGet(HttpServletRequest request, HttpServletResponse response);

    public abstract void doPost(HttpServletRequest request, HttpServletResponse response);

    @Override
    public void service(ServletRequest req, ServletResponse res) throws ServletException, IOException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) req;
        HttpServletResponse httpServletResponse = (HttpServletResponse) res;
        if(HttpMethodType.GET.getCode().equalsIgnoreCase(httpServletRequest.getMethod())) {
            this.doGet(httpServletRequest, httpServletResponse);
            return;
        }

        this.doPost(httpServletRequest, httpServletResponse);
    }

}
