package netty.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import java.util.Random;

public class HeartbeatServerHandle extends SimpleChannelInboundHandler<String> {

  private int unRecPingTimes = 0;
  private static final String HEARTBEAT = "Heartbeat";
  private static final int MAX_UN_REC_PING_TIMES = 3;
  private Random random = new Random(System.currentTimeMillis());

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

    if (null != msg && HEARTBEAT.equals(msg)) {
      System.out
          .println("Client: " + ctx.channel().remoteAddress() + " -- HEARTBEAT INFORMATION-- ");
    } else {
      System.out.println(" Client Request Information ----: " + msg);
      ctx.writeAndFlush("HELLO the number is " + random.nextInt(1000));
    }
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    if (evt instanceof IdleStateEvent) {
      IdleStateEvent event = (IdleStateEvent) evt;
      if (event.state() == IdleState.READER_IDLE) {
        System.out.println("--- Server--- (READER_IDLE time out )");
        if (unRecPingTimes >= MAX_UN_REC_PING_TIMES) {
          System.out.println("--- 服务端---(Read Timeout,  Close chanel)");
          ctx.close();
        } else {
          unRecPingTimes++;
        }
      } else {
        super.userEventTriggered(ctx, evt);
      }
    }
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    super.channelActive(ctx);
    System.out.println("one client Connected");
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    super.channelInactive(ctx);
    System.out.println("one client disconnected");
  }
}
