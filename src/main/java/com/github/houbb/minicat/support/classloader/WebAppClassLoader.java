package com.github.houbb.minicat.support.classloader;


import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * https://www.liaoxuefeng.com/wiki/1545956031987744/1545956487069728
 *
 * 每一个 dir 的 classLoader 独立。
 */
public class WebAppClassLoader extends URLClassLoader {

    private Path classPath;
    private Path[] libJars;

    public WebAppClassLoader(Path classPath, Path libPath) throws IOException {
        super(createUrls(classPath, libPath), ClassLoader.getSystemClassLoader());
//        super("WebAppClassLoader", createUrls(classPath, libPath), ClassLoader.getSystemClassLoader());
//
        this.classPath = classPath.toAbsolutePath().normalize();
        if(libPath.toFile().exists()) {
            this.libJars = Files.list(libPath).filter(p -> p.toString().endsWith(".jar")).map(p -> p.toAbsolutePath().normalize()).sorted().toArray(Path[]::new);
        }
    }

    static URL[] createUrls(Path classPath, Path libPath) throws IOException {
        List<URL> urls = new ArrayList<>();
        urls.add(toDirURL(classPath));

        //lib 可能不存在
        if(libPath.toFile().exists()) {
            Files.list(libPath).filter(p -> p.toString().endsWith(".jar")).sorted().forEach(p -> {
                urls.add(toJarURL(p));
            });
        }

        return urls.toArray(new URL[0]);
    }

    static URL toDirURL(Path p) {
        try {
            if (Files.isDirectory(p)) {
                String abs = toAbsPath(p);
                if (!abs.endsWith("/")) {
                    abs = abs + "/";
                }
                return URI.create("file://" + abs).toURL();
            }
            throw new IOException("Path is not a directory: " + p);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    //D:\github\minicat\src\test\webapps\servlet\WEB-INF\classes
    //D:\github\minicat\src\test\webapps\WEB-INF\classes

    static URL toJarURL(Path p) {
        try {
            if (Files.isRegularFile(p)) {
                String abs = toAbsPath(p);
                return URI.create("file://" + abs).toURL();
            }
            throw new IOException("Path is not a jar file: " + p);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    static String toAbsPath(Path p) throws IOException {
        return p.toAbsolutePath().normalize().toString().replace('\\', '/');
    }

    public static void main(String[] args) throws Exception {
        String path  ="D:\\github\\minicat\\src\\test\\webapps\\servlet\\WEB-INF\\classes";
        String path2  ="D:\\github\\minicat\\src\\test\\webapps\\servlet\\WEB-INF\\lib";
        WebAppClassLoader webAppClassLoader = new WebAppClassLoader(Paths.get(path), Paths.get(path2));

        Class clazz = webAppClassLoader.loadClass("com.github.houbb.servlet.webxml.IndexServlet");
        System.out.println(clazz.getMethods().length);
//        HttpServlet servlet = (HttpServlet) webAppClassLoader.createInstance("com.github.houbb.servlet.webxml.IndexServlet");
    }

}
