//package com.github.houbb.minicat.support.servlet.manager;
//
//import com.github.houbb.heaven.util.lang.StringUtil;
//import com.github.houbb.log.integration.core.Log;
//import com.github.houbb.log.integration.core.LogFactory;
//import com.github.houbb.minicat.exception.MiniCatException;
//import com.github.houbb.minicat.support.classloader.WebAppClassLoader;
//import com.github.houbb.minicat.support.war.WarExtractorDefault;
//import com.github.houbb.minicat.util.InnerResourceUtil;
//import org.dom4j.Document;
//import org.dom4j.Element;
//import org.dom4j.io.SAXReader;
//
//import javax.servlet.http.HttpServlet;
//import java.io.File;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * servlet 管理
// * <p>
// * 基于 web.xml 的读取解析
// *
// * @since 0.5.0
// */
//public class WarsServletManager extends DefaultServletManager {
//
//    private static final Log logger = LogFactory.getLog(WarExtractorDefault.class);
//
//    private String baseDirStr;
//
//    @Override
//    protected void doInit(String baseDirStr) {
//        logger.info("[MiniCat] servlet init with baseDir={}", baseDirStr);
//        this.baseDirStr = baseDirStr;
//
//        // 分别解析 war 包
//        File baseDir = new File(baseDirStr);
//
//        File[] files = baseDir.listFiles();
//        for (File file : files) {
//            if (file.isDirectory()) {
//                handleWarPackage(file);
//            }
//        }
//    }
//
//    /**
//     * 处理单个 war 包
//     *
//     * @param warDir       文件夹
//     */
//    protected void handleWarPackage(File warDir) {
//        logger.info("[MiniCat] handleWarPackage baseDirStr={}, file={}", baseDirStr, warDir);
//        String prefix = "/" + warDir.getName();
//        // 模拟 tomcat，如果在根目录下，war 的名字为 root。则认为是根路径项目。
//        if ("ROOT".equalsIgnoreCase(prefix)) {
//            // 这里需要 / 吗？
//            prefix = "";
//        }
//
//        String webXmlPath = getWebXmlPath(warDir);
//        File webXmlFile = new File(webXmlPath);
//        if (!webXmlFile.exists()) {
//            logger.warn("[MiniCat] webXmlPath={} not found", webXmlPath);
//            return;
//        }
//
//        loadUrlAndServletClass(prefix, webXmlFile, warDir);
//    }
//
//    //1. 解析 web.xml
//    //2. 读取对应的 servlet mapping
//    //3. 保存对应的 url + servlet 示例到 servletMap
//    protected void loadUrlAndServletClass(String urlPrefix,
//                                          File webXmlFile,
//                                          File warDir) {
//        try {
//            SAXReader reader = new SAXReader();
//            Document document = reader.read(webXmlFile);
//
//            Element root = document.getRootElement();
//
//            Map<String, String> servletClassNameMap = new HashMap<>();
//            Map<String, String> urlPatternMap = new HashMap<>();
//            List<Element> servletElements = root.elements("servlet");
//            for (Element servletElement : servletElements) {
//                String servletName = servletElement.elementText("servlet-name");
//                String servletClass = servletElement.elementText("servlet-class");
//                servletClassNameMap.put(servletName, servletClass);
//            }
//
//            List<Element> urlMappingElements = root.elements("servlet-mapping");
//            for (Element urlElem : urlMappingElements) {
//                String servletName = urlElem.elementText("servlet-name");
//                String urlPattern = urlElem.elementText("url-pattern");
//                urlPatternMap.put(servletName, urlPattern);
//            }
//
//            // 自定义 class loader
//            Path classesPath = buildClassesPath(baseDirStr, warDir);
//            Path libPath = buildLibPath(baseDirStr, warDir);
//
//            // 这个是以 web.xml 为主题。
//            try(WebAppClassLoader webAppClassLoader = new WebAppClassLoader(classesPath, libPath)) {
//                // 循环处理
//                for(Map.Entry<String, String> urlPatternEntry : urlPatternMap.entrySet()) {
//                    String servletName = urlPatternEntry.getKey();
//                    String urlPattern = urlPatternEntry.getValue();
//
//                    String className = servletClassNameMap.get(servletName);
//                    if(StringUtil.isEmpty(className)) {
//                        throw new MiniCatException("className not found for servletName: " + servletName);
//                    }
//
//                    // 构建
//                    handleUrlAndClass(urlPrefix, urlPattern, className, webAppClassLoader);
//                }
//            };
//        } catch (Exception e) {
//            throw new MiniCatException(e);
//        }
//    }
//
//    protected void handleUrlAndClass(String urlPrefix,
//                                     String urlPattern,
//                                     String className,
//                                     WebAppClassLoader webAppClassLoader) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
//        Class<?> servletClazz = webAppClassLoader.loadClass(className);
//        HttpServlet httpServlet = (HttpServlet) servletClazz.newInstance();
//
//        super.register(urlPrefix + urlPattern, httpServlet);
//    }
//
//
//    protected Path buildClassesPath(String baseDirStr, File warDir) {
//        String path = InnerResourceUtil.buildFullPath(baseDirStr, warDir.getName() + "/WEB-INF/classes/");
//        return Paths.get(path);
//    }
//
//    protected Path buildLibPath(String baseDirStr, File warDir) {
//        String path = InnerResourceUtil.buildFullPath(baseDirStr, warDir.getName() + "WEB-INF/lib/");
//        return Paths.get(path);
//    }
//
//    protected String buildClassFullPath(String baseDirStr,
//                                        String className) {
//        String prefix = InnerResourceUtil.buildFullPath(baseDirStr, "WEB-INF/classes/");
//
//        String classNamePath = StringUtil.packageToPath(className) + ".class";
//
//        return prefix + classNamePath;
//    }
//
//    protected String getWebXmlPath(File file) {
//        return file.getAbsolutePath() + "/WEB-INF/web.xml";
//    }
//
//}
