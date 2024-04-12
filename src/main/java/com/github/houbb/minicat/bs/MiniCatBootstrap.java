package com.github.houbb.minicat.bs;

import com.github.houbb.log.integration.core.Log;
import com.github.houbb.log.integration.core.LogFactory;
import com.github.houbb.minicat.bs.servlet.MiniCatBootstrapNetty;
import com.github.houbb.minicat.exception.MiniCatException;
import com.github.houbb.minicat.support.attr.DefaultMiniCatAttrManager;
import com.github.houbb.minicat.support.attr.IMiniCatAttrManager;
import com.github.houbb.minicat.support.context.IMiniCatContextInit;
import com.github.houbb.minicat.support.context.LocalMiniCatContextInit;
import com.github.houbb.minicat.support.context.MiniCatContextConfig;
import com.github.houbb.minicat.support.filter.manager.DefaultFilterManager;
import com.github.houbb.minicat.support.filter.manager.IFilterManager;
import com.github.houbb.minicat.support.listener.DefaultListenerManager;
import com.github.houbb.minicat.support.listener.IListenerManager;
import com.github.houbb.minicat.support.request.IRequestDispatcher;
import com.github.houbb.minicat.support.request.RequestDispatcherManager;
import com.github.houbb.minicat.support.servlet.manager.DefaultServletManager;
import com.github.houbb.minicat.support.servlet.manager.IServletManager;
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

import javax.servlet.ServletContextListener;
import java.util.function.Consumer;

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
    private IServletManager servletManager = new DefaultServletManager();

    /**
     * 过滤管理器
     */
    private IFilterManager filterManager = new DefaultFilterManager();

    /**
     * 请求分发
     * @since 0.5.0
     */
    private IRequestDispatcher requestDispatcher = new RequestDispatcherManager();

    /**
     * 上下文初始化
     */
    private IMiniCatContextInit miniCatContextInit = new LocalMiniCatContextInit();

    /**
     * 监听器管理类
     * @since 0.7.0
     */
    private IListenerManager listenerManager = new DefaultListenerManager();

    /**
     * 属性管理
     */
    private IMiniCatAttrManager miniCatAttrManager = new DefaultMiniCatAttrManager();

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

    public void setFilterManager(IFilterManager filterManager) {
        this.filterManager = filterManager;
    }

    public void setMiniCatContextInit(IMiniCatContextInit miniCatContextInit) {
        this.miniCatContextInit = miniCatContextInit;
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

    public void setListenerManager(IListenerManager listenerManager) {
        this.listenerManager = listenerManager;
    }

    public void setMiniCatAttrManager(IMiniCatAttrManager miniCatAttrManager) {
        this.miniCatAttrManager = miniCatAttrManager;
    }

    protected MiniCatContextConfig buildMiniCatContextConfig() {
        MiniCatContextConfig config = new MiniCatContextConfig();
        config.setPort(port);
        config.setServletManager(servletManager);
        config.setFilterManager(filterManager);
        config.setBaseDir(baseDir);
        config.setWarExtractor(warExtractor);
        config.setRequestDispatcher(requestDispatcher);
        config.setListenerManager(listenerManager);
        config.setMiniCatAttrManager(miniCatAttrManager);
        return config;
    }

    public void start() {
        logger.info("[MiniCat] start listen on port {}", port);
        logger.info("[MiniCat] visit url http://{}:{}", "127.0.0.1", port);

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //worker 线程池的数量默认为 CPU 核心数的两倍
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        final MiniCatContextConfig miniCatContextConfig = buildMiniCatContextConfig();
        try {
            logger.info("[MiniCat] config={}", miniCatContextConfig);
            // 初始化
            this.miniCatContextInit.init(miniCatContextConfig);

            // init listener 监听器
            if(miniCatContextConfig != null) {
                listenerManager.getServletContextListeners()
                        .forEach(servletContextListener -> servletContextListener.contextInitialized(miniCatContextConfig));
            }

            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new MiniCatServerHandler(miniCatContextConfig));
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

            // 关闭
            if(miniCatContextConfig != null) {
                listenerManager.getServletContextListeners()
                        .forEach(servletContextListener -> servletContextListener.contextDestroyed(miniCatContextConfig));
            }
        }
    }



}
