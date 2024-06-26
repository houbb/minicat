//package com.github.houbb.minicat.support.servlet.manager;
//
//import com.github.houbb.heaven.support.handler.IHandler;
//import com.github.houbb.log.integration.core.Log;
//import com.github.houbb.log.integration.core.LogFactory;
//import com.github.houbb.minicat.exception.MiniCatException;
//import org.dom4j.Document;
//import org.dom4j.Element;
//import org.dom4j.io.SAXReader;
//
//import javax.servlet.http.HttpServlet;
//import java.io.InputStream;
//import java.util.List;
//
///**
// * servlet 管理
// *
// * 基于 web.xml 的读取解析
// *
// * 默认根路径
// *
// * @since 0.3.0
// */
//public class LocalRootServletManager extends DefaultServletManager {
//
//    private static final Log logger = LogFactory.getLog(LocalRootServletManager.class);
//
//    @Override
//    protected void doInit(String baseDir) {
//        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
//
//        this.loadFromWebXml(resourceAsStream);
//    }
//
//    protected void loadFromWebXml(InputStream resourceAsStream) {
//        this.loadFromWebXml("", resourceAsStream);
//    }
//
//    //1. 解析 web.xml
//    //2. 读取对应的 servlet mapping
//    //3. 保存对应的 url + servlet 示例到 servletMap
//    protected void loadFromWebXml(String urlPrefix, InputStream resourceAsStream) {
//        this.loadFromWebXml(urlPrefix, resourceAsStream, new IHandler<String, Class>() {
//            @Override
//            public Class handle(String s) {
//                try {
//                    // 默认直接加载本地的 class
//                    return Class.forName(s);
//                } catch (ClassNotFoundException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//        });
//    }
//
//    //1. 解析 web.xml
//    //2. 读取对应的 servlet mapping
//    //3. 保存对应的 url + servlet 示例到 servletMap
//    protected void loadFromWebXml(String urlPrefix, InputStream resourceAsStream, IHandler<String, Class> servletClassHandler) {
////        InputStream resourceAsStream = this.getClass().getClassLoader().getResourceAsStream("web.xml");
//        SAXReader saxReader = new SAXReader();
//        try {
//            Document document = saxReader.read(resourceAsStream);
//            Element rootElement = document.getRootElement();
//
//            List<Element> selectNodes = rootElement.selectNodes("//servlet");
//            //1, 找到所有的servlet标签，找到servlet-name和servlet-class
//            //2, 根据servlet-name找到<servlet-mapping>中与其匹配的<url-pattern>
//            for (Element element : selectNodes) {
//                /**
//                 * 1, 找到所有的servlet标签，找到servlet-name和servlet-class
//                 */
//                Element servletNameElement = (Element) element.selectSingleNode("servlet-name");
//                String servletName = servletNameElement.getStringValue();
//                Element servletClassElement = (Element) element.selectSingleNode("servlet-class");
//                String servletClass = servletClassElement.getStringValue();
//
//                /**
//                 * 2, 根据servlet-name找到<servlet-mapping>中与其匹配的<url-pattern>
//                 */
//                //Xpath表达式：从/web-app/servlet-mapping下查询，查询出servlet-name=servletName的元素
//                Element servletMapping = (Element) rootElement.selectSingleNode("/web-app/servlet-mapping[servlet-name='" + servletName + "']'");
//
//                String urlPattern = servletMapping.selectSingleNode("url-pattern").getStringValue();
//                Class servletClazz = servletClassHandler.handle(servletClass);
//                HttpServlet httpServlet = (HttpServlet) servletClazz.newInstance();
//
//                this.register(urlPrefix + urlPattern, httpServlet);
//            }
//
//        } catch (Exception e) {
//            logger.error("[MiniCat] read web.xml failed", e);
//
//            throw new MiniCatException(e);
//        }
//    }
//}
