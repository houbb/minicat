package com.github.houbb.minicat.support.listener.foo;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import javax.servlet.ServletRequestAttributeEvent;
import javax.servlet.ServletRequestAttributeListener;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

public class MyServletRequestAttrListener implements ServletRequestAttributeListener {

    private static final Log logger = LogFactory.getLog(MyServletRequestAttrListener.class);

    @Override
    public void attributeAdded(ServletRequestAttributeEvent srae) {
        logger.info("MyServletRequestAttrListener attributeAdded event={}", srae);
    }

    @Override
    public void attributeRemoved(ServletRequestAttributeEvent srae) {
        logger.info("MyServletRequestAttrListener attributeRemoved event={}", srae);
    }

    @Override
    public void attributeReplaced(ServletRequestAttributeEvent srae) {
        logger.info("MyServletRequestAttrListener attributeReplaced event={}", srae);
    }

}
