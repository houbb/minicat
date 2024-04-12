package com.github.houbb.minicat.support.listener.foo;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import javax.servlet.ServletContextAttributeEvent;
import javax.servlet.ServletContextAttributeListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class MyServletContextAttrListener implements ServletContextAttributeListener {

    private static final Log logger = LogFactory.getLog(MyServletContextAttrListener.class);


    @Override
    public void attributeAdded(ServletContextAttributeEvent event) {
        logger.info("MyServletContextAttrListener attributeAdded, event={}", event);
    }

    @Override
    public void attributeRemoved(ServletContextAttributeEvent event) {
        logger.info("MyServletContextAttrListener attributeAdded, event={}", event);
    }

    @Override
    public void attributeReplaced(ServletContextAttributeEvent event) {
        logger.info("MyServletContextAttrListener attributeAdded, event={}", event);
    }

}
