package com.github.houbb.minicat.support.context;

import com.github.houbb.minicat.support.attr.IMiniCatAttrManager;
import com.github.houbb.minicat.support.filter.manager.IFilterManager;
import com.github.houbb.minicat.support.listener.IListenerManager;
import com.github.houbb.minicat.support.request.IRequestDispatcher;
import com.github.houbb.minicat.support.servlet.manager.IServletManager;
import com.github.houbb.minicat.support.war.IWarExtractor;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import java.util.Enumeration;
import java.util.function.Consumer;

/**
 * @since 0.6.0
 */
public class MiniCatContextConfig extends ServletContextEvent implements IMiniCatAttrManager {

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

    /**
     * 监听器管理类
     *
     * @since 0.7.0
     */
    private IListenerManager listenerManager;

    /**
     * 属性管理
     * @since 0.7.0
     */
    private IMiniCatAttrManager miniCatAttrManager;

    private ServletContext servletContext;

    public IMiniCatAttrManager getMiniCatAttrManager() {
        return miniCatAttrManager;
    }

    public void setMiniCatAttrManager(IMiniCatAttrManager miniCatAttrManager) {
        this.miniCatAttrManager = miniCatAttrManager;
    }

    public IListenerManager getListenerManager() {
        return listenerManager;
    }

    public void setListenerManager(IListenerManager listenerManager) {
        this.listenerManager = listenerManager;
    }

    /**
     * Construct a ServletContextEvent from the given context.
     *
     * @param source - the ServletContext that is sending the event.
     */
    public MiniCatContextConfig(ServletContext source) {
        super(source);
        this.servletContext = source;
    }

    /**
     * Construct a ServletContextEvent from the given context.
     *
     */
    public MiniCatContextConfig() {
        this(new DefaultMiniCatServletContext());
    }

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
    public Object getAttribute(String key) {
        return miniCatAttrManager.getAttribute(key);
    }

    @Override
    public Enumeration<String> getAttributeNames() {
        return miniCatAttrManager.getAttributeNames();
    }

    @Override
    public void setAttribute(String key, Object object) {
        this.miniCatAttrManager.setAttribute(key, object);

        final ServletContextAttributeEvent servletContextAttributeEvent = new ServletContextAttributeEvent(servletContext, key, object);
        // 监听
        this.listenerManager.getServletContextAttributeListeners().forEach(new Consumer<ServletContextAttributeListener>() {
            @Override
            public void accept(ServletContextAttributeListener servletContextAttributeListener) {
                servletContextAttributeListener.attributeAdded(servletContextAttributeEvent);
            }
        });
    }

    @Override
    public Object removeAttribute(String key) {
        Object result = this.miniCatAttrManager.removeAttribute(key);

        final ServletContextAttributeEvent servletContextAttributeEvent = new ServletContextAttributeEvent(servletContext, key, result);
        // 监听
        this.listenerManager.getServletContextAttributeListeners().forEach(new Consumer<ServletContextAttributeListener>() {
            @Override
            public void accept(ServletContextAttributeListener servletContextAttributeListener) {
                servletContextAttributeListener.attributeRemoved(servletContextAttributeEvent);
            }
        });

        return result;
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
                ", listenerManager=" + listenerManager +
                ", miniCatAttrManager=" + miniCatAttrManager +
                "} " + super.toString();
    }

}
