package application;

import netty.NettyServer;

public class NettyApplication {

  public static void main(String[] args)  {
    NettyServer server = new NettyServer("127.0.0.1",8099);
    try {
      server.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
