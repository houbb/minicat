package com.github.houbb.minicat.support.request;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.minicat.bs.MiniCatBootstrap;
import com.github.houbb.minicat.dto.IMiniCatRequest;
import com.github.houbb.minicat.dto.IMiniCatResponse;
import com.github.houbb.minicat.exception.MiniCatException;
import com.github.houbb.minicat.support.context.MiniCatContextConfig;
import com.github.houbb.minicat.support.servlet.manager.IServletManager;
import com.github.houbb.minicat.util.InnerHttpUtil;

import javax.servlet.http.HttpServlet;

/**
 * 静态页面
 */
public class ServletRequestDispatcher implements IRequestDispatcher {

    private static final Log logger = LogFactory.getLog(MiniCatBootstrap.class);

    public void dispatch(final IMiniCatRequest request,
                         final IMiniCatResponse response,
                         final MiniCatContextConfig config) {
        final IServletManager servletManager = config.getServletManager();

        // 直接和 servlet 映射
        final String requestUrl = request.getUrl();
        HttpServlet httpServlet = servletManager.getServlet(requestUrl);
        if(httpServlet == null) {
            logger.warn("[MiniCat] requestUrl={} mapping not found", requestUrl);
            response.write(InnerHttpUtil.http404Resp());
        } else {
            // 正常的逻辑处理
            try {
                httpServlet.service(request, response);
            } catch (Exception e) {
                logger.error("[MiniCat] http servlet handle meet ex", e);

                throw new MiniCatException(e);
            }
        }
    }

}
