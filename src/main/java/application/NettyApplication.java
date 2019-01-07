package application;

import netty.NettyServer;

/**
 * netty server
 */
public class NettyApplication {

  public static void main(String[] args)  {
    NettyServer server = new NettyServer("10.0.90.45",8099);
    try {
      server.start();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
