package com.github.houbb.minicat.bs.socket;

import com.github.houbb.minicat.bs.servlet.MiniCatBootstrapNioThreadSocket;

public class MiniCatBootstrapNioThreadSocketTest {

    public static void main(String[] args) {
        MiniCatBootstrapNioThreadSocket nioSocket = new MiniCatBootstrapNioThreadSocket();
        nioSocket.start();
    }

}
