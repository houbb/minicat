package com.github.houbb.minicat.dto;

import javax.servlet.http.HttpServletRequest;

/**
 * @since 0.4.0
 */
public interface IMiniCatRequest extends HttpServletRequest {

    /**
     * 获取请求地址
     * @return 请求地址
     */
    String getUrl();

    /**
     * 获取方法
     * @return 请求方法
     */
    String getMethod();

}
