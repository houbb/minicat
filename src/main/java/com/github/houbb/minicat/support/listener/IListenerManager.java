package com.github.houbb.minicat.support.listener;

import javax.servlet.*;
import java.util.EventListener;
import java.util.List;

public interface IListenerManager {

    void addEventListener(final EventListener eventListener);

    List<ServletContextListener> getServletContextListeners();

    List<ServletContextAttributeListener> getServletContextAttributeListeners();

    List<ServletRequestListener> getServletRequestListeners();

    List<ServletRequestAttributeListener> getServletRequestAttributeListeners();

    List<ReadListener> getReadListeners();

    List<WriteListener> getWriteListeners();

}
