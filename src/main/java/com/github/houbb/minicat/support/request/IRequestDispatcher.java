package com.github.houbb.minicat.support.request;

public interface IRequestDispatcher {

    /**
     * 请求分发
     * @param context 上下文
     */
    void dispatch(RequestDispatcherContext context);

}
