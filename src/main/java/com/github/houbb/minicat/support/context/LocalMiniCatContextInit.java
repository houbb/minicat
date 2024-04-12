package com.github.houbb.minicat.support.context;

import com.github.houbb.heaven.util.lang.StringUtil;
import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.minicat.bs.servlet.MiniCatBootstrapNetty;
import com.github.houbb.minicat.exception.MiniCatException;
import com.github.houbb.minicat.support.classloader.WebAppClassLoader;
import com.github.houbb.minicat.support.filter.manager.IFilterManager;
import com.github.houbb.minicat.support.servlet.manager.IServletManager;
import com.github.houbb.minicat.support.war.IWarExtractor;
import com.github.houbb.minicat.util.InnerResourceUtil;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import javax.servlet.Filter;
import javax.servlet.http.HttpServlet;
import java.io.File;
import java.nio.file.Path;
import java.util.*;

public class LocalMiniCatContextInit implements IMiniCatContextInit {

    private static final Log logger = LogFactory.getLog(LocalMiniCatContextInit.class);

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

    protected void beforeProcessWebXml() {
        final String baseDir = miniCatContextConfig.getBaseDir();
        final IWarExtractor warExtractor = miniCatContextConfig.getWarExtractor();

        logger.info("[MiniCat] beforeProcessWebXml start baseDir={}", miniCatContextConfig.getBaseDir());

        //1. 加载解析所有的 war 包
        //2. 解压 war 包
        //3. 解析对应的 servlet 映射关系
        warExtractor.extract(baseDir);

        logger.info("[MiniCat] beforeProcessWebXml end");
    }

    /**
     * 初始化
     *
     * @param config 配置
     */
    public void init(final MiniCatContextConfig config) {
        this.miniCatContextConfig = config;

        beforeProcessWebXml();

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

            //3. 处理 listener
            initListener(root);
        } catch (Exception e) {
            throw new MiniCatException(e);
        }
    }

    /**
     * 初始化监听器
     * @since 0.7.0
     */
    protected void initListener(Element root) {
        try {
            List<Element> filterElements = root.elements("listener");
            List<String> servletClassList = new ArrayList<>();
            for (Element servletElement : filterElements) {
                String servletClass = servletElement.elementText("listener-class");
                servletClassList.add(servletClass);
            }

            // 这个是以 web.xml 为主题。
            servletClassList.forEach(className -> {
                try {
                    Class<?> servletClazz = Class.forName(className);
                    EventListener eventListener = (EventListener) servletClazz.newInstance();
                    miniCatContextConfig.getListenerManager().addEventListener(eventListener);
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
                    throw new MiniCatException(e);
                }
            });
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
