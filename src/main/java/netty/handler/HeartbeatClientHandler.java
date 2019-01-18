package netty.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoop;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import netty.client.HeartbeatClient;

public class HeartbeatClientHandler extends SimpleChannelInboundHandler<String> {

  private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:dd");

  private HeartbeatClient client;

  public HeartbeatClientHandler(HeartbeatClient client) {
    this.client = client;
  }

  private static final ByteBuf HEARTBEAT_SEQUENCE = Unpooled
      .unreleasableBuffer(Unpooled.copiedBuffer("Heartbeat",
          CharsetUtil.UTF_8));

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

    System.out.println("Receive the server request:" + msg);

    if ("Heartbeat".equals(msg)) {
      ctx.write("has read message from server");
      ctx.flush();
    }
    ReferenceCountUtil.release(msg);
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    if (evt instanceof IdleStateEvent) {
      IdleState state = ((IdleStateEvent) evt).state();
      if (state == IdleState.WRITER_IDLE) {
        ctx.writeAndFlush(HEARTBEAT_SEQUENCE.duplicate());
      }
    } else {
      super.userEventTriggered(ctx, evt);
    }
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    super.channelInactive(ctx);
    System.out.println("客户端与服务端断开连接,断开的时间为：" + format.format(new Date()));

    final EventLoop eventLoop = ctx.channel().eventLoop();
    eventLoop.schedule(new Runnable() {
      @Override
      public void run() {
        client.doConncet();
      }
    }, 10, TimeUnit.SECONDS);
  }
}

