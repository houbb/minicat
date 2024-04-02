# 项目简介

简易版本的 tomcat 实现。

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.houbb/minicat/badge.svg)](http://mvnrepository.com/artifact/com.github.houbb/minicat)
[![Build Status](https://www.travis-ci.org/houbb/minicat.svg?branch=master)](https://www.travis-ci.org/houbb/minicat?branch=master)
[![Coverage Status](https://coveralls.io/repos/github/houbb/minicat/badge.svg?branch=master)](https://coveralls.io/github/houbb/minicat?branch=master)

# 特性

- 简单的启动实现

# 变更日志

> [变更日志](CHANGE_LOG.md)

# 快速开始

## maven 依赖

```xml
<dependency>
    <groupId>com.github.houbb</groupId>
    <artifactId>minicat</artifactId>
    <version>0.1.0</version>
</dependency>
```

## 启动测试

```java
package com.github.houbb.minicat.bs;

import java.util.concurrent.TimeUnit;

public class MiniCatBootstrapMain {

    public static void main(String[] args) throws InterruptedException {
        MiniCatBootstrap bootstrap = new MiniCatBootstrap();
        bootstrap.start();

        System.out.println("main START sleep");
        TimeUnit.SECONDS.sleep(10);
        System.out.println("main END sleep");

        bootstrap.stop();
    }

}
```

# ROAD-MAP

- [] NIO 实现
- [] 加载 war 包
- [] 内嵌支持？