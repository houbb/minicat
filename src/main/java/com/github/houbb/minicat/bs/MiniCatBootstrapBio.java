package com.github.houbb.minicat.bs;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.minicat.dto.MiniCatRequest;
import com.github.houbb.minicat.dto.MiniCatResponse;
import com.github.houbb.minicat.exception.MiniCatException;
import com.github.houbb.minicat.support.request.IRequestDispatcher;
import com.github.houbb.minicat.support.request.RequestDispatcherContext;
import com.github.houbb.minicat.support.request.RequestDispatcherManager;
import com.github.houbb.minicat.support.servlet.IServletManager;
import com.github.houbb.minicat.support.servlet.WebXmlServletManager;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @since 0.1.0
 * @author 老马啸西风
 */
public class MiniCatBootstrapBio {

    private static final Log logger = LogFactory.getLog(MiniCatBootstrapBio.class);

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

    /**
     * servlet 管理
     *
     * @since 0.3.0
     */
    private IServletManager servletManager = new WebXmlServletManager();

    /**
     * 请求分发
     *
     * @since 0.3.0
     */
    private IRequestDispatcher requestDispatcher = new RequestDispatcherManager();

    public IRequestDispatcher getRequestDispatcher() {
        return requestDispatcher;
    }

    public void setRequestDispatcher(IRequestDispatcher requestDispatcher) {
        this.requestDispatcher = requestDispatcher;
    }

    public IServletManager getServletManager() {
        return servletManager;
    }

    public void setServletManager(IServletManager servletManager) {
        this.servletManager = servletManager;
    }

    public MiniCatBootstrapBio(int port) {
        this.port = port;
    }

    public MiniCatBootstrapBio() {
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
                //TRW
                try (Socket socket = serverSocket.accept()) {
                    // 出入参
                    InputStream inputStream = socket.getInputStream();
                    MiniCatRequest request = new MiniCatRequest(inputStream);
                    MiniCatResponse response = new MiniCatResponse(socket.getOutputStream());

                    // 分发处理
                    final RequestDispatcherContext dispatcherContext = new RequestDispatcherContext();
                    dispatcherContext.setRequest(request);
                    dispatcherContext.setResponse(response);
                    dispatcherContext.setServletManager(servletManager);
                    requestDispatcher.dispatch(dispatcherContext);
                } catch (Exception e) {
                    logger.error("[MiniCat] server meet ex", e);
                    //TODO: 如何保持健壮性？
                }
            }

            logger.info("[MiniCat] end listen on port {}", port);
        } catch (Exception e) {
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
