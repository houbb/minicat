package com.github.houbb.minicat.bs;

public class MiniCatBootstrapMainWithWarsTest {

    public static void main(String[] args) throws InterruptedException {
        String baseWarDir = "D:\\github\\minicat\\src\\test\\webapps";
        MiniCatBootstrap bootstrap = new MiniCatBootstrap(8080, baseWarDir);
        bootstrap.start();
    }

}
