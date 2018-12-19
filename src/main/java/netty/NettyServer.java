package netty;

import helper.PortCheckerHelper;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import netty.ssl.HttpsInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/***
 * netty 服务端
 */
public class NettyServer {

  private Logger logger = LoggerFactory.getLogger(this.getClass());
  //private static NettyServerHandle handler = new NettyServerHandle();
  private Thread thread = null;

  EventLoopGroup bossGroup = new NioEventLoopGroup();
  EventLoopGroup workerGroup = new NioEventLoopGroup();

  private String ip;
  private int port;

  public NettyServer(String ip, int port) {
    this.ip = ip;
    this.port = port;
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
          ServerBootstrap boot = new ServerBootstrap();
          boot.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
              .option(ChannelOption.SO_BACKLOG, 100)
              .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
              .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
              .handler(new LoggingHandler(LogLevel.INFO))
              .childHandler(new HttpsInitializer());
              //.childHandler(new NettyServerInitializer(handler));

          /*Start the Server*/
          ChannelFuture future = boot.bind(address1).sync();
          logger.info("Netty Server start success!{" + addressInfo + "}");
          // Wait until the server socket is closed.
          future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
          e.printStackTrace();
          logger.error("NettyServer start fail{" + addressInfo + "}");
        } finally {
          workerGroup.shutdownGracefully();
          bossGroup.shutdownGracefully();
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
