package netty.init;


import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.timeout.IdleStateHandler;
import netty.client.HeartbeatClient;
import netty.handler.HeartbeatClientHandler;

public class HeartbeatClientInitializer extends ChannelInitializer<SocketChannel> {

  private HeartbeatClient client;

  public HeartbeatClientInitializer(HeartbeatClient client) {
    this.client = client;
  }

  @Override
  protected void initChannel(SocketChannel socketChannel) throws Exception {
    ChannelPipeline pipeline = socketChannel.pipeline();
    pipeline.addLast(new IdleStateHandler(0, 5, 0));
    pipeline.addLast("encoder", new StringEncoder());
    pipeline.addLast("decoder", new StringDecoder());
    pipeline.addLast("handler", new HeartbeatClientHandler(client));
  }
}
