package com.github.houbb.minicat.bs.socket;

import com.github.houbb.minicat.bs.servlet.MiniCatBootstrapNetty;

public class MiniCatBootstrapNettyTest {

    public static void main(String[] args) throws InterruptedException {
        MiniCatBootstrapNetty bootstrap = new MiniCatBootstrapNetty();
        bootstrap.start();
    }

}
