package com.github.houbb.minicat.util;

public class InnerResourceUtil {

    // 获取当前线程的上下文类加载器的资源路径
    public static String getCurrentThreadContextClassLoaderResource() {
        return Thread.currentThread().getContextClassLoader().getResource("").getPath();
    }

    // 获取系统类加载器的资源路径
    public static String getSystemClassLoaderResource() {
        return ClassLoader.getSystemResource("").getPath();
    }

    // 获取指定类加载器的资源路径
    public static String getClassLoaderResource(Class<?> clazz) {
        return clazz.getClassLoader().getResource("").getPath();
    }

    // 获取指定类的根路径
    public static String getClassRootResource(Class<?> clazz) {
        return clazz.getResource("/").getPath();
    }

    // 获取指定类的路径
    public static String getClassResource(Class<?> clazz) {
        return clazz.getResource("").getPath();
    }

    // 获取当前工作目录的路径
    public static String getCurrentWorkingDirectory() {
        return System.getProperty("user.dir");
    }

    // 获取类路径的路径
    public static String getClassPath() {
        return System.getProperty("java.class.path");
    }


    /**
     * 构建完整的路径。
     * 给定一个前缀和路径，此方法将两者结合形成一个完整的URL或文件路径。
     * 如果前缀末尾没有斜杠，则会在两者之间添加一个斜杠，确保路径的正确拼接。
     *
     * @param prefix 路径前缀。例如，http://example.com或C:\Users。
     * @param path 相对路径。例如，path/to/resource或test.txt。
     * @return 拼接后的完整路径。确保路径正确连接，不会因缺少斜杠而导致路径错误。
     */
    public static String buildFullPath(String prefix, String path) {
        // 检查前缀是否已以斜杠结尾，如果是，则直接拼接路径；如果不是，则在前缀后添加斜杠再拼接路径
        if(path.startsWith("/")) {
            path = path.substring(1);
        }

        if(prefix.endsWith("/")) {
            return prefix + path;
        } else {
            return prefix + "/" + path;
        }
    }

    public static void main(String[] args) {
        // 示例用法
        System.out.println("Current Thread Context ClassLoader Resource: " + getCurrentThreadContextClassLoaderResource());
        System.out.println("System ClassLoader Resource: " + getSystemClassLoaderResource());
        System.out.println("Class Loader Resource: " + getClassLoaderResource(InnerResourceUtil.class));
        System.out.println("Class Root Resource: " + getClassRootResource(InnerResourceUtil.class));
        System.out.println("Class Resource: " + getClassResource(InnerResourceUtil.class));
        System.out.println("Current Working Directory: " + getCurrentWorkingDirectory());
        System.out.println("Class Path: " + getClassPath());
    }

}
