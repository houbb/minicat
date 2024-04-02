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

        System.out.println("main START sleep");
        TimeUnit.SECONDS.sleep(10);
        System.out.println("main END sleep");

        bootstrap.stop();
    }

}
