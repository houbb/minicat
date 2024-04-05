package com.github.houbb.minicat.bs.socket;

import com.github.houbb.minicat.bs.servlet.MiniCatBootstrapNioSocket;

public class MiniCatBootstrapNioSocketTest {

    public static void main(String[] args) {
        MiniCatBootstrapNioSocket nioSocket = new MiniCatBootstrapNioSocket();
        nioSocket.start();
    }

}
