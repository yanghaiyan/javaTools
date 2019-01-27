package netty.init;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.timeout.IdleStateHandler;
import java.util.concurrent.TimeUnit;
import netty.handler.HeartbeatServerHandle;

/**
 *
 * 服务端
 */
public class HeartbeatServerInitializer extends ChannelInitializer<SocketChannel> {

  /* 检测chanel是否接受过心跳数据时间间隔（单位秒）*/
  private static final int READ_WAIT_SECONDS = 10;

  ChannelInboundHandlerAdapter handlerAdapter = null;

  public HeartbeatServerInitializer(ChannelInboundHandlerAdapter handlerAdapter) {
    this.handlerAdapter = handlerAdapter;
  }

  @Override
  protected void initChannel(SocketChannel ch) throws Exception {

    ChannelPipeline pipeline = ch.pipeline();

    //监听读操作，读超时时间5秒，超过5秒关闭channel
    pipeline.addLast("ping", new IdleStateHandler(READ_WAIT_SECONDS, 0, 0, TimeUnit.SECONDS));
    pipeline.addLast("decoder", new StringDecoder());
    pipeline.addLast("encoder", new StringDecoder());

    pipeline.addLast("handler", new HeartbeatServerHandle());
  }
}
