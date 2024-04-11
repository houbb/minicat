//package com.github.houbb.minicat.support.servlet.manager;
//
//import com.github.houbb.heaven.util.lang.StringUtil;
//import com.github.houbb.log.integration.core.Log;
//import com.github.houbb.log.integration.core.LogFactory;
//import com.github.houbb.minicat.exception.MiniCatException;
//import org.dom4j.Document;
//import org.dom4j.Element;
//import org.dom4j.io.SAXReader;
//
//import javax.servlet.Filter;
//import javax.servlet.http.HttpServlet;
//import java.io.File;
//import java.io.InputStream;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * servlet 管理
// *
// * 基于 web.xml 的读取解析
// *
// * 默认根路径
// *
// * @since 0.6.0
// */
//public class LocalServletManager extends DefaultServletManager {
//
//    private static final Log logger = LogFactory.getLog(LocalServletManager.class);
//
//    /**
//     * web 文件地址
//     */
//    protected final File webXmlFile;
//
//    /**
//     * 请求地址前缀
//     */
//    private final String urlPrefix;
//
//    public LocalServletManager(String webXmlPath, String urlPrefix) {
//        this.webXmlFile = new File(webXmlPath);
//        this.urlPrefix = urlPrefix;
//    }
//
//    public LocalServletManager(String webXmlPath) {
//        this(webXmlPath, "");
//    }
//
//    @Override
//    protected void doInit(String baseDir) {
//        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
//
//        this.processWebXml();
//    }
//
//
//
//}
