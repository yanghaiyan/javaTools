package netty;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import helper.PortCheckerHelper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import netty.handler.HeartbeatServerHandle;
import netty.init.HeartbeatServerInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * netty 服务端
 */
public class NettyServer {

  private Logger logger = LoggerFactory.getLogger(this.getClass());
  //private static NettyServerHandle handler = new NettyServerHandle();
  private Thread thread = null;

  EventLoopGroup bossGroup;
  EventLoopGroup workerGroup;

  private String ip;
  private int port;

  public NettyServer(String ip, int port) {
    this.ip = ip;
    this.port = port;
    bossGroup = Epoll.isAvailable() ? (new EpollEventLoopGroup(0,
        new ThreadFactoryBuilder().setDaemon(true).setNameFormat("Netty-EpollEvent-Listener-%d")
            .build())) : new NioEventLoopGroup();
    workerGroup = Epoll.isAvailable() ? (new EpollEventLoopGroup(0,
        new ThreadFactoryBuilder().setDaemon(true)
            .setNameFormat("Netty-WorkerEpollEvent-Listener-%d").build()))
        : new NioEventLoopGroup();
  }

  public void start() throws Exception {
    /**
     * 检查端口
     */
    if (!PortCheckerHelper.checkPort("", port)) {
      throw new Exception("请确保内部端口" + port + "没有被占用");
    }
    InetSocketAddress address = null;
    if (ip.equals("*")) {
      address = new InetSocketAddress(port);
    } else {
      address = new InetSocketAddress(InetAddress.getByName(ip), port);
    }
    final InetSocketAddress address1 = address;
    final String addressInfo = ip + ":" + port;
    thread = new Thread() {

      @Override
      public void run() {
        super.run();
        try {
          ServerBootstrap bootstrap = new ServerBootstrap();
          bootstrap.group(bossGroup, workerGroup).channel(
              Epoll.isAvailable() ? EpollServerSocketChannel.class : NioServerSocketChannel.class)
              .option(ChannelOption.SO_BACKLOG, 100)
              /**boss 内存池.*/
              .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
              /** work 内存池.*/
              .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
              .handler(new LoggingHandler(LogLevel.INFO))
              // .childHandler(new HttpsInitializer());
              .childHandler(new HeartbeatServerInitializer(new HeartbeatServerHandle()));

          if (Epoll.isAvailable()) {
            bootstrap.option(EpollChannelOption.SO_REUSEPORT, true);
            // linux系统下使用SO_REUSEPORT特性，使得多个线程绑定同一个端口
            int cpuNum = Runtime.getRuntime().availableProcessors() / 2;
            logger.info("using epoll reuseport and cpu:" + cpuNum);
            for (int i = 0; i < cpuNum; i++) {
              ChannelFuture future = bootstrap.bind(address1).await();
              if (!future.isSuccess()) {
                throw new Exception("bootstrap bind fail port is " + address1);
              }
              logger.info(" bind cpu core " + i + "! { " + address1.toString() + " }");
            }
          } else {
            ChannelFuture f = bootstrap.bind(address1).sync();
            logger.info(" start success! { " + address1.toString() + " }");
            f.channel().closeFuture().sync();
          }
        } catch (Exception e) {
          e.printStackTrace();
          logger.error("NettyServer start fail{" + addressInfo + "}");
        } finally {
          Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
              workerGroup.shutdownGracefully();
              bossGroup.shutdownGracefully();
            }
          });
        }
      }
    };
    thread.start();
  }

  public void stop() throws Exception {
    try {
      workerGroup.shutdownGracefully();
      bossGroup.shutdownGracefully();
    } catch (Exception ex) {
      logger.error("Netty Service stop fail");
    }
    logger.info("Netty Service stop success!");
    thread.interrupt();
  }

}
