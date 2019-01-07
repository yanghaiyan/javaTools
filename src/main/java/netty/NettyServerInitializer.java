package netty;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * http协议
 */
public class NettyServerInitializer extends ChannelInitializer<SocketChannel> {

  ChannelInboundHandlerAdapter handlerAdapter = null;

  public NettyServerInitializer(ChannelInboundHandlerAdapter handlerAdapter) {
    this.handlerAdapter = handlerAdapter;
  }

  @Override
  protected void initChannel(SocketChannel ch) throws Exception {

    ChannelPipeline p = ch.pipeline();
    // decoder
    p.addLast("decoder", new HttpRequestDecoder());
    // 使用HttpObjectAggregator将http请求合并成一个FullHttpRequest ，
    // 包括HttpRequest,HttpContent,HttpLastContent
    // 解决拆包和分包的问题
    p.addLast("aggegator", new HttpObjectAggregator(1024 * 1024 * 64));
    // encoder
    p.addLast("encoder", new HttpResponseEncoder());
    p.addLast("chunked", new ChunkedWriteHandler());
    // main handler
    p.addLast("handler", handlerAdapter);
    // ip filter
  }
}
