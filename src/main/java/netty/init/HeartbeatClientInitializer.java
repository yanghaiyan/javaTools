package netty.init;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import netty.client.HeartbeatClient;
import netty.handler.HeartbeatClientHandler;

/**
 * 客户端心跳
 */
public class HeartbeatClientInitializer extends ChannelInitializer<SocketChannel> {

  private HeartbeatClient client;

  public HeartbeatClientInitializer(HeartbeatClient client) {
    this.client = client;
  }

  @Override
  protected void initChannel(SocketChannel socketChannel) throws Exception {
    ChannelPipeline pipeline = socketChannel.pipeline();

    // 是实现心跳的关键, 它会根据不同的 IO idle 类型来产生不同的 IdleStateEvent 事件, 而这个事件的捕获, 其实就是在 userEventTriggered 方法中实现的.
    pipeline.addLast(new IdleStateHandler(0, 5, 0));
    pipeline.addLast("encoder", new StringEncoder());
    pipeline.addLast("decoder", new StringDecoder());
    pipeline.addLast("handler", new HeartbeatClientHandler(client));
  }
}
