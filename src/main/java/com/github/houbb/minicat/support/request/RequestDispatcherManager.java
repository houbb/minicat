package com.github.houbb.minicat.support.request;

import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.minicat.dto.IMiniCatRequest;
import com.github.houbb.minicat.dto.IMiniCatResponse;
import com.github.houbb.minicat.support.context.MiniCatContextConfig;
import com.github.houbb.minicat.support.servlet.manager.IServletManager;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

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
    public void dispatch(final IMiniCatRequest request,
                         final IMiniCatResponse response,
                         final MiniCatContextConfig config) {
        final IServletManager servletManager = config.getServletManager();


        // 判断文件是否存在
        String requestUrl = request.getUrl();
        //before
        List<Filter> filterList = config.getFilterManager().getMatchFilters(requestUrl);

        // 获取请求分发
        final IRequestDispatcher requestDispatcher = getRequestDispatcher(requestUrl);

        // 请求前
        filterList.forEach(filter -> {
            try {
                filter.doFilter(request, response, null);
            } catch (IOException | ServletException e) {
                throw new RuntimeException(e);
            }
        });

        // 正式分发
        requestDispatcher.dispatch(request, response, config);
    }

    private IRequestDispatcher getRequestDispatcher(String requestUrl) {
        if (StringUtil.isEmpty(requestUrl)) {
            return emptyRequestDispatcher;
        } else {
            if (requestUrl.endsWith(".html")) {
                return staticHtmlRequestDispatcher;
            } else {
                return servletRequestDispatcher;
            }
        }
    }

}
