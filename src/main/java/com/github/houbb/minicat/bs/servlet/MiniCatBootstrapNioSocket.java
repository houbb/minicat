package com.github.houbb.minicat.bs.servlet;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.minicat.exception.MiniCatException;
import com.github.houbb.minicat.util.InnerHttpUtil;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MiniCatBootstrapNioSocket {

    private static final Log logger = LogFactory.getLog(MiniCatBootstrapNioSocket.class);

    private final int port;
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    public MiniCatBootstrapNioSocket() {
        this.port = 8080;
    }

    public void start() {
        logger.info("[MiniCat] start listen on port {}", port);
        logger.info("[MiniCat] visit url http://{}:{}", "127.0.0.1", port);

        try {
            serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(new InetSocketAddress(port));
            serverSocketChannel.configureBlocking(false);

            selector = Selector.open();
            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

            while (true) {
                int readyChannels = selector.select();
                if (readyChannels == 0) {
                    continue;
                }

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

                while (keyIterator.hasNext()) {
                    SelectionKey key = keyIterator.next();

                    if (key.isAcceptable()) {
                        handleAccept(key);
                    } else if (key.isReadable()) {
                        handleRead(key);
                    }

                    keyIterator.remove();
                }
            }
        } catch (IOException | InterruptedException e) {
            logger.error("[MiniCat] start meet ex", e);
            throw new MiniCatException(e);
        }
    }

    private void handleAccept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel socketChannel = serverSocketChannel.accept();
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
    }

    private void handleRead(SelectionKey key) throws IOException, InterruptedException {
        logger.info("handle read start");
        SocketChannel socketChannel = (SocketChannel) key.channel();
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        StringBuilder requestBuilder = new StringBuilder();

        int bytesRead = socketChannel.read(buffer);
        while (bytesRead > 0) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                requestBuilder.append((char) buffer.get());
            }
            buffer.clear();
            bytesRead = socketChannel.read(buffer);
        }

        String requestString = requestBuilder.toString();
        logger.info("handle read requestString={}", requestString);

        TimeUnit.SECONDS.sleep(5); // 模拟耗时操作

        logger.info("start write");
        writeToClient(socketChannel, requestString);
        logger.info("end writeToClient");

        socketChannel.close();
    }

    private void writeToClient(SocketChannel socketChannel, String requestString) throws IOException {
        String httpText = InnerHttpUtil.http200Resp("ECHO: \r\n" + requestString);
        ByteBuffer buffer = ByteBuffer.wrap(httpText.getBytes("UTF-8"));
        socketChannel.write(buffer);
    }

}
