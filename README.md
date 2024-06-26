# 项目简介

```
 /\_/\  
( o.o ) 
 > ^ <
```

mini-cat 是简易版本的 tomcat 实现。别称【嗅虎】(心有猛虎，轻嗅蔷薇。)

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.houbb/minicat/badge.svg)](http://mvnrepository.com/artifact/com.github.houbb/minicat)
[![Build Status](https://www.travis-ci.org/houbb/minicat.svg?branch=master)](https://www.travis-ci.org/houbb/minicat?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/houbb/minicat/badge.svg?branch=master)](https://coveralls.io/github/houbb/minicat?branch=master)

# 特性

- 简单的启动实现/netty 支持

- servlet 支持

- 静态网页支持

- filter/listener 支持

- wars 支持

# 变更日志

> [变更日志](CHANGE_LOG.md)

# 快速开始

## maven 依赖

```xml
<dependency>
    <groupId>com.github.houbb</groupId>
    <artifactId>minicat</artifactId>
    <version>0.7.0</version>
</dependency>
```

## 启动测试

运行测试类 `MiniCatBootstrapMain#main`

```java
MiniCatBootstrap bootstrap = new MiniCatBootstrap();
bootstrap.start();
```

启动日志：

```
[INFO] [2024-04-03 11:09:15.178] [main] [c.g.h.m.s.s.WebXmlServletManager.register] - [MiniCat] register servlet, url=/my, servlet=com.github.houbb.minicat.support.servlet.MyMiniCatHttpServlet
[INFO] [2024-04-03 11:09:15.180] [Thread-0] [c.g.h.m.b.MiniCatBootstrap.startSync] - [MiniCat] start listen on port 8080
[INFO] [2024-04-03 11:09:15.180] [Thread-0] [c.g.h.m.b.MiniCatBootstrap.startSync] - [MiniCat] visit url http://127.0.0.1:8080
```

页面访问：[http://127.0.0.1:8080](http://127.0.0.1:8080)

响应：

```
http://127.0.0.1:8080
```

## 测试

servlet: http://127.0.0.1:8080/my

html: http://127.0.0.1:8080/index.html

# 系列教程

[从零手写实现 apache Tomcat-01-入门介绍](https://houbb.github.io/2016/11/07/web-server-tomcat-02-hand-write-overview)

[从零手写实现 apache Tomcat-02-web.xml 入门详细介绍](https://houbb.github.io/2016/11/07/web-server-tomcat-02-hand-write-web-xml)

[从零手写实现 tomcat-03-基本的 socket 实现](https://houbb.github.io/2016/11/07/web-server-tomcat-03-hand-write-simple-socket)

[从零手写实现 tomcat-04-请求和响应的抽象](https://houbb.github.io/2016/11/07/web-server-tomcat-04-hand-write-request-and-resp)

[从零手写实现 tomcat-05-servlet 处理支持](https://houbb.github.io/2016/11/07/web-server-tomcat-05-hand-write-servlet-web-xml)

[从零手写实现 tomcat-06-servlet bio/thread/nio/netty 池化处理](https://houbb.github.io/2016/11/07/web-server-tomcat-06-hand-write-thread-pool)

[从零手写实现 tomcat-07-war 如何解析处理三方的 war 包？](https://houbb.github.io/2016/11/07/web-server-tomcat-07-hand-write-war)

[从零手写实现 tomcat-08-tomcat 如何与 springboot 集成？](https://houbb.github.io/2016/11/07/web-server-tomcat-08-hand-write-embed)

[从零手写实现 tomcat-09-servlet 处理类](https://houbb.github.io/2016/11/07/web-server-tomcat-09-hand-write-servlet)

[从零手写实现 tomcat-10-static resource 静态资源文件](https://houbb.github.io/2016/11/07/web-server-tomcat-10-hand-write-static-resource)

[从零手写实现 tomcat-11-filter 过滤器](https://houbb.github.io/2016/11/07/web-server-tomcat-11-hand-write-filter)

[从零手写实现 tomcat-12-listener 监听器](https://houbb.github.io/2016/11/07/web-server-tomcat-12-hand-write-listener)


# ROAD-MAP

- [x] servlet 标准支持
- [x] 请求线程池支持
- [x] NIO 实现
- [x] netty 实现
- [x] 加载 war 包
- [x] listener 的实现
- [] error/welcome 页面？
- [] printWriter 等兼容不够优雅
- [] 内嵌支持？
- [] session
- [] 注解的解析支持
