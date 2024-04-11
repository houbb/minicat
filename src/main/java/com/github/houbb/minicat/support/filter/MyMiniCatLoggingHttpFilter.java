package com.github.houbb.minicat.support.filter;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpFilter;
import java.io.IOException;

/**
 * 
 * @since 0.6.0
 */
public class MyMiniCatLoggingHttpFilter extends HttpFilter {

    private static final Log logger = LogFactory.getLog(MyMiniCatLoggingHttpFilter.class);

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        logger.info("[MiniCat] MyMiniCatLoggingHttpFilter#doFilter req={}, resp={}", req, res);

        super.doFilter(req, res, chain);
    }

}
