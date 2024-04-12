package com.github.houbb.minicat.support.listener.foo;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

public class MyServletRequestListener implements ServletRequestListener {

    private static final Log logger = LogFactory.getLog(MyServletRequestListener.class);

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        logger.info("MyServletRequestListener requestDestroyed");
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        logger.info("MyServletRequestListener requestInitialized");
    }

}
