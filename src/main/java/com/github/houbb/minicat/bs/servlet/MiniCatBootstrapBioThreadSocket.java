package com.github.houbb.minicat.bs.servlet;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.minicat.exception.MiniCatException;
import com.github.houbb.minicat.util.InnerHttpUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 实际测试还是会阻塞
 *
 * @author 老马啸西风
 * @since 0.1.0
 */
public class MiniCatBootstrapBioThreadSocket {

    private static final Log logger = LogFactory.getLog(MiniCatBootstrapBioThreadSocket.class);

    /**
     * 启动端口号
     */
    private final int port;

    /**
     * 服务端 socket
     */
    private ServerSocket serverSocket;

    private final ExecutorService threadPool;

    public MiniCatBootstrapBioThreadSocket() {
        this.port = 8080;

        threadPool = Executors.newFixedThreadPool(10);
    }

    public void start() {
        logger.info("[MiniCat] start listen on port {}", port);
        logger.info("[MiniCat] visit url http://{}:{}", "127.0.0.1", port);

        try {
            this.serverSocket = new ServerSocket(port);

            while (true) {
                Socket clientSocket = serverSocket.accept(); // 等待客户端连接

                // 从Socket获取输入流
                threadPool.submit(new Runnable() {
                    @Override
                    public void run() {
                        handleSocket(clientSocket);
                    }
                });
            }


        } catch (Exception e) {
            logger.error("[MiniCat] start meet ex", e);
            throw new MiniCatException(e);
        }
    }

    private void handleSocket(Socket clientSocket) {
        try {
            logger.info("readRequestString start");
            String requestString = readRequestString(clientSocket);
            logger.info("readRequestString end");

            // 这里模拟一下耗时呢
            TimeUnit.SECONDS.sleep(5);

            // 写回到客户端
            logger.info("writeToClient start");
            writeToClient(clientSocket, requestString);
            logger.info("writeToClient end");

            // 关闭连接
            clientSocket.close();
        } catch (IOException | InterruptedException e) {
            logger.error("");
        }
    }

    private void writeToClient(Socket clientSocket, String requestString) throws IOException {
        OutputStream outputStream = clientSocket.getOutputStream();
        String httpText = InnerHttpUtil.http200Resp("ECHO: \r\n" + requestString);
        outputStream.write(httpText.getBytes("UTF-8"));
    }

    private String readRequestString(Socket clientSocket) throws IOException {
        // 从Socket获取输入流
        BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        StringBuilder requestBuilder = new StringBuilder();
        String line;
        // 读取HTTP请求直到空行（表示HTTP请求结束）
        while ((line = reader.readLine()) != null && !line.isEmpty()) {
            requestBuilder.append(line).append("\n");
        }
        return requestBuilder.toString();
    }

}
