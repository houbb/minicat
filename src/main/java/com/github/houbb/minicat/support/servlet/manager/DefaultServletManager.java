package com.github.houbb.minicat.support.servlet.manager;

import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.minicat.exception.MiniCatException;

import javax.servlet.http.HttpServlet;
import java.util.HashMap;
import java.util.Map;

/**
 * servlet 管理
 *
 * 基于 web.xml 的读取解析
 * @since 0.5.0
 */
public class DefaultServletManager implements IServletManager {

    private static final Log logger = LogFactory.getLog(DefaultServletManager.class);

    protected final Map<String, HttpServlet> servletMap = new HashMap<>();

    protected String baseDir;

    protected void doInit(String baseDirStr) {
        this.baseDir = baseDirStr;
    }

    @Override
    public void init(String baseDir) {
        if(StringUtil.isEmpty(baseDir)) {
            throw new MiniCatException("baseDir is empty!");
        }

        doInit(baseDir);
    }

    @Override
    public void register(String url, HttpServlet servlet) {
        logger.info("[MiniCat] register servlet, url={}, servlet={}", url, servlet.getClass().getName());

        servletMap.put(url, servlet);
    }

    @Override
    public HttpServlet getServlet(String url) {
        return servletMap.get(url);
    }



}
