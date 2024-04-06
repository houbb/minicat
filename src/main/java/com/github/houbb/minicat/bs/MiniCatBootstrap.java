package com.github.houbb.minicat.bs;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.minicat.bs.servlet.MiniCatBootstrapNetty;
import com.github.houbb.minicat.exception.MiniCatException;
import com.github.houbb.minicat.support.request.IRequestDispatcher;
import com.github.houbb.minicat.support.request.RequestDispatcherManager;
import com.github.houbb.minicat.support.servlet.IServletManager;
import com.github.houbb.minicat.support.servlet.LocalServletManager;
import com.github.houbb.minicat.support.servlet.WarsServletManager;
import com.github.houbb.minicat.support.war.IWarExtractor;
import com.github.houbb.minicat.support.war.WarExtractorDefault;
import com.github.houbb.minicat.util.InnerResourceUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @since 0.1.0
 * @author 老马啸西风
 */
public class MiniCatBootstrap {

    private static final Log logger = LogFactory.getLog(MiniCatBootstrapNetty.class);

    /**
     * 启动端口号
     */
    private final int port;

    /**
     * 默认文件夹
     * @since 0.5.0
     */
    private String baseDir = InnerResourceUtil.getClassRootResource(MiniCatBootstrap.class);

    /**
     * war 解压管理
     *
     * @since 0.5.0
     */
    private IWarExtractor warExtractor = new WarExtractorDefault();

    /**
     * servlet 管理
     *
     * @since 0.5.0
     */
    private IServletManager servletManager = new WarsServletManager();

    /**
     * 请求分发
     * @since 0.5.0
     */
    private IRequestDispatcher requestDispatcher = new RequestDispatcherManager();

    public MiniCatBootstrap(final int port, final String baseDir) {
        this.port = 8080;
        this.baseDir = baseDir;
    }

    public MiniCatBootstrap(final int port) {
        this.port = port;
    }

    public MiniCatBootstrap() {
        this(8080);
    }

    public void setBaseDir(String baseDir) {
        this.baseDir = baseDir;
    }

    public void setWarExtractor(IWarExtractor warExtractor) {
        this.warExtractor = warExtractor;
    }

    public void setServletManager(IServletManager servletManager) {
        this.servletManager = servletManager;
    }

    public void setRequestDispatcher(IRequestDispatcher requestDispatcher) {
        this.requestDispatcher = requestDispatcher;
    }

    public void start() {
        beforeStart();

        logger.info("[MiniCat] start listen on port {}", port);
        logger.info("[MiniCat] visit url http://{}:{}", "127.0.0.1", port);

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //worker 线程池的数量默认为 CPU 核心数的两倍
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new MiniCatServerHandler(servletManager, requestDispatcher, baseDir));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections.
            ChannelFuture future = serverBootstrap.bind(port).sync();

            // Wait until the server socket is closed.
            future.channel().closeFuture().sync();
            logger.info("DONE");
        } catch (InterruptedException e) {
            logger.error("[MiniCat] start meet ex", e);
            throw new MiniCatException(e);
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    protected void beforeStart() {
        logger.info("[MiniCat] beforeStart baseDir={}", baseDir);

        //1. 加载解析所有的 war 包
        //2. 解压 war 包
        //3. 解析对应的 servlet 映射关系
        warExtractor.extract(baseDir);

        // 初始化 servlet 映射关系
        servletManager.init(baseDir);
    }

}
