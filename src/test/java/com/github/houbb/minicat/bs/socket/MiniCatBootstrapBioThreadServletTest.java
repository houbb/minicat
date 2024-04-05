package com.github.houbb.minicat.bs.socket;

import com.github.houbb.minicat.bs.servlet.MiniCatBootstrapBioThreadSocket;

public class MiniCatBootstrapBioThreadServletTest {

    public static void main(String[] args) throws InterruptedException {
        MiniCatBootstrapBioThreadSocket bootstrap = new MiniCatBootstrapBioThreadSocket();
        bootstrap.start();
    }

}
