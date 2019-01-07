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
 * httpЭ��
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
    // ʹ��HttpObjectAggregator��http����ϲ���һ��FullHttpRequest ��
    // ����HttpRequest,HttpContent,HttpLastContent
    // �������ͷְ�������
    p.addLast("aggegator", new HttpObjectAggregator(1024 * 1024 * 64));
    // encoder
    p.addLast("encoder", new HttpResponseEncoder());
    p.addLast("chunked", new ChunkedWriteHandler());
    // main handler
    p.addLast("handler", handlerAdapter);
    // ip filter
  }
}
