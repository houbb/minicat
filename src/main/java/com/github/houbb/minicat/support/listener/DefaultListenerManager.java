package com.github.houbb.minicat.support.listener;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.minicat.exception.MiniCatException;
import com.github.houbb.minicat.support.listener.foo.MyServletContextAttrListener;

import javax.servlet.*;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class DefaultListenerManager implements IListenerManager {

    private static final Log logger = LogFactory.getLog(DefaultListenerManager.class);

    private final List<ServletContextListener> servletContextListeners = new ArrayList<>();
    private final List<ServletContextAttributeListener> servletContextAttributeListeners = new ArrayList<>();
    private final List<ServletRequestListener> servletRequestListeners = new ArrayList<>();
    private final List<ServletRequestAttributeListener> servletRequestAttributeListeners = new ArrayList<>();
    private final List<ReadListener> readListeners = new ArrayList<>();
    private final List<WriteListener> writeListeners = new ArrayList<>();

    @Override
    public void addEventListener(EventListener eventListener) {
        logger.info("Start add event ={}", eventListener.getClass().getName());

        if(eventListener instanceof ServletContextListener) {
            servletContextListeners.add((ServletContextListener) eventListener);
        } else if(eventListener instanceof ServletContextAttributeListener) {
            servletContextAttributeListeners.add((ServletContextAttributeListener) eventListener);
        } else if(eventListener instanceof ServletRequestListener) {
            servletRequestListeners.add((ServletRequestListener) eventListener);
        } else if(eventListener instanceof ServletRequestAttributeListener) {
            servletRequestAttributeListeners.add((ServletRequestAttributeListener) eventListener);
        } else if(eventListener instanceof ReadListener) {
            readListeners.add((ReadListener) eventListener);
        } else if(eventListener instanceof WriteListener) {
            writeListeners.add((WriteListener) eventListener);
        } else {
            throw new MiniCatException("Not support type of eventListener=" + eventListener.getClass().getName());
        }
    }

    @Override
    public List<ServletContextListener> getServletContextListeners() {
        return servletContextListeners;
    }

    @Override
    public List<ServletContextAttributeListener> getServletContextAttributeListeners() {
        return servletContextAttributeListeners;
    }

    @Override
    public List<ServletRequestListener> getServletRequestListeners() {
        return servletRequestListeners;
    }

    @Override
    public List<ServletRequestAttributeListener> getServletRequestAttributeListeners() {
        return servletRequestAttributeListeners;
    }

    @Override
    public List<ReadListener> getReadListeners() {
        return readListeners;
    }

    @Override
    public List<WriteListener> getWriteListeners() {
        return writeListeners;
    }
}
