package com.github.houbb.minicat;

import com.github.houbb.minicat.bs.MiniCatBootstrap;

import java.util.concurrent.TimeUnit;

/**
 * 这个文件的位置很重要。
 */
public class MiniCatBootstrapMain {

    public static void main(String[] args) throws InterruptedException {
        MiniCatBootstrap bootstrap = new MiniCatBootstrap();
        bootstrap.start();
    }

}
