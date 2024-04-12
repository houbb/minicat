package com.github.houbb.minicat.support.listener.foo;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.io.IOException;

public class MyServletReadListener implements ReadListener {

    private static final Log logger = LogFactory.getLog(MyServletReadListener.class);

    @Override
    public void onDataAvailable() throws IOException {
        logger.info("MyServletReadListener onDataAvailable");
    }

    @Override
    public void onAllDataRead() throws IOException {
        logger.info("MyServletReadListener onAllDataRead");
    }

    @Override
    public void onError(Throwable t) {
        logger.error("MyServletReadListener onError", t);
    }

}
