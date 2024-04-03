package com.github.houbb.minicat.support.request;

import com.github.houbb.heaven.util.io.FileUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.minicat.bs.MiniCatBootstrap;
import com.github.houbb.minicat.dto.MiniCatRequest;
import com.github.houbb.minicat.dto.MiniCatResponse;
import com.github.houbb.minicat.exception.MiniCatException;
import com.github.houbb.minicat.support.servlet.IServletManager;
import com.github.houbb.minicat.util.InnerHttpUtil;
import com.github.houbb.minicat.util.ResourceUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.io.IOException;

/**
 * 静态页面
 */
public class ServletRequestDispatcher implements IRequestDispatcher {

    private static final Log logger = LogFactory.getLog(MiniCatBootstrap.class);

    /**
     * 请求分发
     *
     * @param context 上下文
     */
    public void dispatch(RequestDispatcherContext context) {
        final MiniCatRequest request = context.getRequest();
        final MiniCatResponse response = context.getResponse();
        final IServletManager servletManager = context.getServletManager();

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
