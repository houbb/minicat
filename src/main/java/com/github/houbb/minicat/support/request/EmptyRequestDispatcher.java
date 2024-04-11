package com.github.houbb.minicat.support.request;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.minicat.bs.MiniCatBootstrap;
import com.github.houbb.minicat.dto.IMiniCatRequest;
import com.github.houbb.minicat.dto.IMiniCatResponse;
import com.github.houbb.minicat.support.context.MiniCatContextConfig;
import com.github.houbb.minicat.util.InnerHttpUtil;

/**
 * 空请求
 */
public class EmptyRequestDispatcher implements IRequestDispatcher{

    private static final Log logger = LogFactory.getLog(MiniCatBootstrap.class);

    /**
     * 请求分发
     */
    @Override
    public void dispatch(final IMiniCatRequest request,
                         final IMiniCatResponse response,
                         final MiniCatContextConfig config) {
        logger.warn("[MiniCat] empty request url");

        //也可以返回默认页面之类的
        response.write(InnerHttpUtil.http400Resp());
    }

}
