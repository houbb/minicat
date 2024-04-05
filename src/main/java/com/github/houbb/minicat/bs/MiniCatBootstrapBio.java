package com.github.houbb.minicat.bs;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.minicat.dto.MiniCatRequestBio;
import com.github.houbb.minicat.dto.MiniCatResponseBio;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private final ExecutorService threadPool;

    public MiniCatBootstrapBio(int port, int threadPoolSize) {
        this.port = port;
        this.threadPool = Executors.newFixedThreadPool(threadPoolSize);
    }

    public MiniCatBootstrapBio(int port) {
        this(port, 10);
    }

    public MiniCatBootstrapBio() {
        this(8080);
    }

    public void start() {
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
                Socket socket = serverSocket.accept();
                // 提交到线程池处理
                threadPool.execute(new RequestHandler(socket));
            }

            logger.info("[MiniCat] end listen on port {}", port);
        } catch (Exception e) {
            logger.error("[MiniCat] start meet ex", e);
            throw new MiniCatException(e);
        }
    }

    private class RequestHandler implements Runnable {
        private final Socket socket;

        public RequestHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                InputStream inputStream = socket.getInputStream();
                MiniCatRequestBio request = new MiniCatRequestBio(inputStream);
                MiniCatResponseBio response = new MiniCatResponseBio(socket.getOutputStream());

                final RequestDispatcherContext dispatcherContext = new RequestDispatcherContext();
                dispatcherContext.setRequest(request);
                dispatcherContext.setResponse(response);
                dispatcherContext.setServletManager(servletManager);
                requestDispatcher.dispatch(dispatcherContext);

                socket.close();
            } catch (IOException e) {
                logger.error("[MiniCat] error handling request", e);
            }
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
