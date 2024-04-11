package com.github.houbb.minicat.support.context;

import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.minicat.exception.MiniCatException;
import com.github.houbb.minicat.support.filter.manager.IFilterManager;
import com.github.houbb.minicat.support.servlet.manager.IServletManager;
import com.github.houbb.minicat.util.InnerResourceUtil;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocalMiniCatContextInit implements IMiniCatContextInit {

    private final File webXmlFile;

    private final String urlPrefix;

    private MiniCatContextConfig miniCatContextConfig;

    public LocalMiniCatContextInit(File webXmlFile, String urlPrefix) {
        this.webXmlFile = webXmlFile;
        this.urlPrefix = urlPrefix;
    }

    public LocalMiniCatContextInit(File webXmlFile) {
        this(webXmlFile, "");
    }

    public LocalMiniCatContextInit() {
        this(new File(InnerResourceUtil.buildFullPath(InnerResourceUtil.getClassLoaderResource(LocalMiniCatContextInit.class),
                "web.xml")));
    }

    /**
     * 初始化
     *
     * @param config 配置
     */
    public void init(final MiniCatContextConfig config) {
        this.miniCatContextConfig = config;

        // 解析 war 处理
        processWebXml();
    }

    /**
     * 处理 web 文件
     */
    protected void processWebXml() {
        try {
            SAXReader reader = new SAXReader();
            Document document = reader.read(webXmlFile);

            Element root = document.getRootElement();

            // 1. 处理 servlet
            final IServletManager servletManager = this.miniCatContextConfig.getServletManager();
            processWebServlet(root, servletManager);

            //2. 处理 filter
            final IFilterManager filterManager = this.miniCatContextConfig.getFilterManager();
            processWebFilter(root, filterManager);
        } catch (Exception e) {
            throw new MiniCatException(e);
        }
    }

    protected void processWebFilter(Element root, final IFilterManager filterManager) {
        Map<String, String> servletClassNameMap = new HashMap<>();
        Map<String, String> urlPatternMap = new HashMap<>();
        List<Element> servletElements = root.elements("filter");
        for (Element servletElement : servletElements) {
            String servletName = servletElement.elementText("filter-name");
            String servletClass = servletElement.elementText("filter-class");
            servletClassNameMap.put(servletName, servletClass);
        }
        List<Element> urlMappingElements = root.elements("filter-mapping");
        for (Element urlElem : urlMappingElements) {
            String servletName = urlElem.elementText("filter-name");
            String urlPattern = urlElem.elementText("url-pattern");
            urlPatternMap.put(servletName, urlPattern);
        }
        handleFilterConfigMap(servletClassNameMap, urlPatternMap, filterManager);
    }

    protected void handleFilterConfigMap(Map<String, String> filterClassNameMap, Map<String, String> urlPatternMap, final IFilterManager filterManager) {
        try {
            for (Map.Entry<String, String> urlPatternEntry : urlPatternMap.entrySet()) {
                String filterName = urlPatternEntry.getKey();
                String urlPattern = urlPatternEntry.getValue();

                String className = filterClassNameMap.get(filterName);
                if (StringUtil.isEmpty(className)) {
                    throw new MiniCatException("className not found for filterName: " + filterName);
                }

                Class servletClazz = Class.forName(className);
                Filter httpServlet = (Filter) servletClazz.newInstance();

                // 构建
                String fullUrlPattern = buildFullUrlPattern(urlPattern);
                filterManager.register(fullUrlPattern, httpServlet);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new MiniCatException(e);
        }
    }


    protected String buildFullUrlPattern(String urlPattern) {
        return urlPrefix + urlPattern;
    }

    protected void processWebServlet(Element root, final IServletManager servletManager) {
        Map<String, String> servletClassNameMap = new HashMap<>();
        Map<String, String> urlPatternMap = new HashMap<>();
        List<Element> servletElements = root.elements("servlet");
        for (Element servletElement : servletElements) {
            String servletName = servletElement.elementText("servlet-name");
            String servletClass = servletElement.elementText("servlet-class");
            servletClassNameMap.put(servletName, servletClass);
        }
        List<Element> urlMappingElements = root.elements("servlet-mapping");
        for (Element urlElem : urlMappingElements) {
            String servletName = urlElem.elementText("servlet-name");
            String urlPattern = urlElem.elementText("url-pattern");
            urlPatternMap.put(servletName, urlPattern);
        }
        handleServletConfigMap(servletClassNameMap, urlPatternMap, servletManager);
    }

    protected void handleServletConfigMap(Map<String, String> servletClassNameMap, Map<String, String> urlPatternMap, final IServletManager servletManager) {
        try {
            for (Map.Entry<String, String> urlPatternEntry : urlPatternMap.entrySet()) {
                String servletName = urlPatternEntry.getKey();
                String urlPattern = urlPatternEntry.getValue();

                String className = servletClassNameMap.get(servletName);
                if (StringUtil.isEmpty(className)) {
                    throw new MiniCatException("className not found for servletName: " + servletName);
                }

                Class servletClazz = Class.forName(className);
                HttpServlet httpServlet = (HttpServlet) servletClazz.newInstance();

                // 构建
                String fullUrlPattern = buildFullUrlPattern(urlPattern);
                servletManager.register(fullUrlPattern, httpServlet);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new MiniCatException(e);
        }
    }


}
