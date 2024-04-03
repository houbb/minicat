package com.github.houbb.minicat.support.request;

import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.minicat.dto.MiniCatRequest;
import com.github.houbb.minicat.dto.MiniCatResponse;
import com.github.houbb.minicat.support.servlet.IServletManager;

/**
 * 请求分发管理器
 *
 * @since 0.3.0
 */
public class RequestDispatcherManager implements IRequestDispatcher {

    private static final Log logger = LogFactory.getLog(RequestDispatcherManager.class);

    private final IRequestDispatcher emptyRequestDispatcher = new EmptyRequestDispatcher();
    private final IRequestDispatcher servletRequestDispatcher = new ServletRequestDispatcher();
    private final IRequestDispatcher staticHtmlRequestDispatcher = new StaticHtmlRequestDispatcher();

    @Override
    public void dispatch(RequestDispatcherContext context) {
        final MiniCatRequest request = context.getRequest();
        final MiniCatResponse response = context.getResponse();
        final IServletManager servletManager = context.getServletManager();

        // 判断文件是否存在
        String requestUrl = request.getUrl();
        if (StringUtil.isEmpty(requestUrl)) {
            emptyRequestDispatcher.dispatch(context);
        } else {
            if (requestUrl.endsWith(".html")) {
                staticHtmlRequestDispatcher.dispatch(context);
            } else {
                servletRequestDispatcher.dispatch(context);
            }
        }
    }

}
