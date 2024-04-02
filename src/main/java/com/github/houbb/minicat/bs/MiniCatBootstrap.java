package com.github.houbb.minicat.bs;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.minicat.dto.MiniCatRequest;
import com.github.houbb.minicat.dto.MiniCatResponse;
import com.github.houbb.minicat.exception.MiniCatException;
import com.github.houbb.minicat.util.InnerHttpUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @since 0.1.0
 * @author 老马啸西风
 */
public class MiniCatBootstrap {

    private static final Log logger = LogFactory.getLog(MiniCatBootstrap.class);

    /**
     * 启动端口号
     */
    private final int port;

    /**
     * 是否运行的标识
     */
    private volatile boolean runningFlag = false;

    /**
     * 服务端 socket
     */
    private ServerSocket serverSocket;

    public MiniCatBootstrap(int port) {
        this.port = port;
    }

    public MiniCatBootstrap() {
        this(8080);
    }

    public synchronized void start() {
        // 引入线程池
        Thread serverThread = new Thread(new Runnable() {
            @Override
            public void run() {
                startSync();
            }
        });

        // 启动
        serverThread.start();
    }

    /**
     * 服务的启动
     */
    public void startSync() {
        if(runningFlag) {
            logger.warn("[MiniCat] server is already start!");
            return;
        }

        logger.info("[MiniCat] start listen on port {}", port);
        logger.info("[MiniCat] visit url http://{}:{}", "127.0.0.1", port);

        try {
            this.serverSocket = new ServerSocket(port);
            runningFlag = true;

            while(runningFlag && !serverSocket.isClosed()){
                Socket socket = serverSocket.accept();
                // 输入流
                InputStream inputStream = socket.getInputStream();
                MiniCatRequest request = new MiniCatRequest(inputStream);

                // 输出流
                MiniCatResponse response = new MiniCatResponse(socket.getOutputStream());
                response.write(InnerHttpUtil.httpResp("Hello miniCat!").getBytes());

                socket.close();
            }

            logger.info("[MiniCat] end listen on port {}", port);
        } catch (IOException e) {
            logger.error("[MiniCat] start meet ex", e);
            throw new MiniCatException(e);
        }
    }

    /**
     * 服务的暂停
     */
    public void stop() {
        logger.info("[MiniCat] stop called!");

        if(!runningFlag) {
            logger.warn("[MiniCat] server is not start!");
            return;
        }

        try {
            if(this.serverSocket != null) {
                serverSocket.close();
            }
            this.runningFlag = false;

            logger.info("[MiniCat] stop listen on port {}", port);
        } catch (IOException e) {
            logger.error("[MiniCat] stop meet ex", e);
            throw new MiniCatException(e);
        }
    }

}
