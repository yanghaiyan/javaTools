package netty.ssl;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslHandler;
import javax.net.ssl.SSLEngine;
import netty.NettyServerHandle;

public class HttpsInitializer extends ChannelInitializer<SocketChannel> {

  @Override
  protected void initChannel(SocketChannel ch) throws Exception {
    ChannelPipeline p = ch.pipeline();
    // decoder
    // ssl 环境
    SSLEngine sslEngine = ContextSSLFactory.getInstance().getSslEngine();
    p.addLast("ssl", new SslHandler(sslEngine));

    p.addLast("decoder", new HttpRequestDecoder());
    // 使用HttpObjectAggregator将http请求合并成一个FullHttpRequest ，
    // 包括HttpRequest,HttpContent,HttpLastContent
    // 解决拆包和分包的问题
    p.addLast("aggegator", new HttpObjectAggregator(1024 * 1024 * 64));
    // encoder
    p.addLast("encoder", new HttpResponseEncoder());
    // main handler
    p.addLast("handler", new NettyServerHandle());
  }
}
