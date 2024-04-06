package com.github.houbb.minicat.support.servlet;

import java.io.InputStream;

/**
 * servlet 管理
 *
 * 基于 web.xml 的读取解析
 * @since 0.3.0
 */
public class LocalServletManager extends AbstractServletManager {

    @Override
    protected void doInit(String baseDir) {
        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");

        super.loadFromWebXml(resourceAsStream);
    }

}
