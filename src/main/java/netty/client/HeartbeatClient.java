package netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import netty.init.HeartbeatClientInitializer;

public class HeartbeatClient {

  private Random random = new Random();
  public Channel channel;
  public Bootstrap bootstrap;

  protected String host = "127.0.0.1";
  protected int port = 8887;

  public void run() throws Exception {
    EventLoopGroup group = new NioEventLoopGroup();
    try {
      bootstrap = new Bootstrap();
      bootstrap.group(group)
          .channel(NioSocketChannel.class)
          .handler(new HeartbeatClientInitializer(HeartbeatClient.this));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void sendData() throws Exception {
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    while (true) {
      String cmd = in.readLine();
      switch (cmd) {
        case "close":
          channel.close();
          break;
        default:
          channel.writeAndFlush(in.readLine());
          break;
      }
    }
  }

  /**
   * ���ӷ����
   *  <P>
   *    ������ͻ��˺ͷ������� TCP ���ӵĽ���, ���ҵ� TCP ����ʧ��ʱ,
   *    doConnect �� ͨ�� "quartz().eventLoop().schedule" ����ʱ10s ������������.
   *  </P>
   */
  public void doConncet() {
    if (channel != null && channel.isActive()) {
      return;
    }
    ChannelFuture channelFuture = bootstrap.connect(host, port);
    channelFuture.addListener(new ChannelFutureListener() {
      @Override
      public void operationComplete(ChannelFuture futureListener) throws Exception {
        if (channelFuture.isSuccess()) {
          channel = futureListener.channel();
          System.out.println("connect server successfully");
        } else {
          System.out.println("Failed to connect to server, try connect after 10s");
          futureListener.channel().eventLoop().schedule(new Runnable() {
            @Override
            public void run() {
              doConncet();
            }
          }, 10, TimeUnit.SECONDS);
        }
      }
    });

  }
}
