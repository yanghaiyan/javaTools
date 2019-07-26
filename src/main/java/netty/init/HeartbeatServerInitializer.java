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
 * �����
 */
public class HeartbeatServerInitializer extends ChannelInitializer<SocketChannel> {

  /* ���chanel�Ƿ���ܹ���������ʱ��������λ�룩*/
  private static final int READ_WAIT_SECONDS = 10;

  ChannelInboundHandlerAdapter handlerAdapter = null;

  public HeartbeatServerInitializer(ChannelInboundHandlerAdapter handlerAdapter) {
    this.handlerAdapter = handlerAdapter;
  }

  @Override
  protected void initChannel(SocketChannel ch) throws Exception {

    ChannelPipeline pipeline = ch.pipeline();

    //����������������ʱʱ��5�룬����5��ر�quartz
    pipeline.addLast("ping", new IdleStateHandler(READ_WAIT_SECONDS, 0, 0, TimeUnit.SECONDS));
    pipeline.addLast("decoder", new StringDecoder());
    pipeline.addLast("encoder", new StringDecoder());

    pipeline.addLast("handler", new HeartbeatServerHandle());
  }
}
