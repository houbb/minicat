package com.github.houbb.minicat.support.listener.foo;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyServletContextListener implements ServletContextListener {

    private static final Log logger = LogFactory.getLog(MyServletContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.info("MyServletContextListener contextInitialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("MyServletContextListener contextDestroyed");
    }

}
