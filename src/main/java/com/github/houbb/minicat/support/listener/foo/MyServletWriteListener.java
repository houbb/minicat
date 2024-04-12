package com.github.houbb.minicat.support.listener.foo;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.WriteListener;
import java.io.IOException;

public class MyServletWriteListener implements WriteListener {

    private static final Log logger = LogFactory.getLog(MyServletWriteListener.class);

    @Override
    public void onWritePossible() throws IOException {
        logger.info("MyServletWriteListener onWritePossible");
    }

    @Override
    public void onError(Throwable t) {
        logger.error("MyServletWriteListener onError", t);
    }

}
