package com.github.houbb.minicat.bs.socket;

import com.github.houbb.minicat.bs.servlet.MiniCatBootstrapBioSocket;

public class MiniCatBootstrapBioServletTest {

    public static void main(String[] args) throws InterruptedException {
        MiniCatBootstrapBioSocket bootstrap = new MiniCatBootstrapBioSocket();
        bootstrap.start();
    }

}
