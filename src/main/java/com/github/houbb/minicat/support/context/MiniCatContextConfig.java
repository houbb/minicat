package com.github.houbb.minicat.support.context;

import com.github.houbb.minicat.support.filter.manager.IFilterManager;
import com.github.houbb.minicat.support.request.IRequestDispatcher;
import com.github.houbb.minicat.support.servlet.manager.IServletManager;
import com.github.houbb.minicat.support.war.IWarExtractor;

/**
 * @since 0.6.0
 */
public class MiniCatContextConfig {

    /**
     * 启动端口号
     */
    private int port;

    /**
     * 默认文件夹
     * @since 0.5.0
     */
    private String baseDir;

    /**
     * war 解压管理
     *
     * @since 0.5.0
     */
    private IWarExtractor warExtractor;

    /**
     * servlet 管理
     *
     * @since 0.5.0
     */
    private IServletManager servletManager;

    /**
     * 过滤管理器
     */
    private IFilterManager filterManager;

    /**
     * 请求分发
     * @since 0.5.0
     */
    private IRequestDispatcher requestDispatcher;

    public IWarExtractor getWarExtractor() {
        return warExtractor;
    }

    public void setWarExtractor(IWarExtractor warExtractor) {
        this.warExtractor = warExtractor;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getBaseDir() {
        return baseDir;
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public IServletManager getServletManager() {
        return servletManager;
    }

    public void setServletManager(IServletManager servletManager) {
        this.servletManager = servletManager;
    }

    public IFilterManager getFilterManager() {
        return filterManager;
    }

    public void setFilterManager(IFilterManager filterManager) {
        this.filterManager = filterManager;
    }

    public IRequestDispatcher getRequestDispatcher() {
        return requestDispatcher;
    }

    public void setRequestDispatcher(IRequestDispatcher requestDispatcher) {
        this.requestDispatcher = requestDispatcher;
    }

    @Override
    public String toString() {
        return "MiniCatContextConfig{" +
                "port=" + port +
                ", baseDir='" + baseDir + '\'' +
                ", warExtractor=" + warExtractor +
                ", servletManager=" + servletManager +
                ", filterManager=" + filterManager +
                ", requestDispatcher=" + requestDispatcher +
                '}';
    }

}
