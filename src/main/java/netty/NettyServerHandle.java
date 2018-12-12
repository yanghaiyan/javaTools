package netty;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpMethod.POST;

import exception.BasicException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * @author YHY
 */
public class NettyServerHandle extends SimpleChannelInboundHandler<FullHttpRequest> {

  private static final String APPLICATION_XML = "application/xml";
  private static final String TEXT_XML = "text/xml";
  private static final String TEXT_PLAIN = "text/plain";


  @Override
  protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {

    HttpResponseStatus responseStatus = HttpResponseStatus.OK;
    FullHttpRequest request = msg;
    InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
    String uri = request.uri();

  }


  /**
   * checkout uri
   */
  private void checkoutUrl(String uri, String getUrl, HttpResponseStatus responseStatus)
      throws BasicException {
    if (!uri.equals(getUrl)) {
      responseStatus = HttpResponseStatus.NOT_FOUND;
      throw new BasicException("http路径错误");
    }
  }

  /***
   * Checkout method
   * @param method
   * @param responseStatus
   */
  private void checkoutMethod(HttpMethod method, HttpResponseStatus responseStatus) {
    if (POST.equals(method)) {
      //TODO
    } else if (GET.equals(method)) {
      //TODO
    } else {
      //TODO
    }
  }

  /**
   * Checkout Content-Type
   */
  private void checkoutContentType(List<String> contentType, HttpResponseStatus responseStatus)
      throws BasicException {
    if (!(contentType.contains(APPLICATION_XML) || contentType.contains(TEXT_XML))) {
      responseStatus = HttpResponseStatus.UNSUPPORTED_MEDIA_TYPE;
      throw new BasicException("http消息类型不支持");
    }
  }

  private String getContent(FullHttpRequest request, HttpResponseStatus respStatus)
      throws BasicException {
    // 请求的content部分的bytes
    byte[] reqBytes;
    String reqStr = "";
    // 请求的content部分 ByteBuf
    ByteBuf bufReq = request.content();

    if (bufReq.readableBytes() > 0) {
      // 如果有内容
      int length = bufReq.readableBytes();
      reqBytes = new byte[length];
      if ((length != 0) && (bufReq.isReadable())) {
        bufReq.getBytes(0, reqBytes, 0, length);
      }
    } else {
      respStatus = HttpResponseStatus.BAD_REQUEST;
      throw new BasicException("http消息为空");
    }

    try {
      reqStr = new String(reqBytes, "UTF-8");
    } catch (UnsupportedEncodingException e) {
      respStatus = HttpResponseStatus.BAD_REQUEST;
      throw new BasicException("http消息解析失败");
    }

    return reqStr;
  }

}