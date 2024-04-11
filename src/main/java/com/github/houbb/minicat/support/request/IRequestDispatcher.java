package com.github.houbb.minicat.support.request;

import com.github.houbb.minicat.dto.IMiniCatRequest;
import com.github.houbb.minicat.dto.IMiniCatResponse;
import com.github.houbb.minicat.support.context.MiniCatContextConfig;

public interface IRequestDispatcher {

    /**
     * 请求分发
     * @param config 配置
     * @param request 请求
     * @param response 响应
     */
    void dispatch(final IMiniCatRequest request,
                  final IMiniCatResponse response,
                  final MiniCatContextConfig config);


}
