package com.github.houbb.minicat.support.request;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.minicat.bs.MiniCatBootstrap;
import com.github.houbb.minicat.dto.MiniCatRequest;
import com.github.houbb.minicat.dto.MiniCatResponse;
import com.github.houbb.minicat.util.InnerHttpUtil;

/**
 * 空请求
 */
public class EmptyRequestDispatcher implements IRequestDispatcher{

    private static final Log logger = LogFactory.getLog(MiniCatBootstrap.class);

    /**
     * 请求分发
     */
    public void dispatch(RequestDispatcherContext context) {
        logger.warn("[MiniCat] empty request url");

        //也可以返回默认页面之类的
        context.getResponse().write(InnerHttpUtil.http400Resp());
    }

}
