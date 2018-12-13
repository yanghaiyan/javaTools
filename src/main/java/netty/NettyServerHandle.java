package netty;

import static io.netty.buffer.Unpooled.wrappedBuffer;
import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpMethod.POST;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

import com.alibaba.fastjson.JSON;
import exception.BasicException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderValues;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpUtil;
import io.netty.util.ReferenceCountUtil;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import netty.entity.RequestEntity;
import netty.entity.ResponseEntity;

/**
 * @author YHY
 */
public class NettyServerHandle extends SimpleChannelInboundHandler<FullHttpRequest> {

  private static final String APPLICATION_JSON = "application/json";
  private static final String TEXT_JSON = "text/json";
  private static final String TEXT_PLAIN = "text/plain";
  String URL = "/post";


  @Override
  protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {

    HttpResponseStatus respStatus = HttpResponseStatus.OK;
    FullHttpRequest request = msg;
    try {
      InetSocketAddress remoteAddress = (InetSocketAddress) ctx.channel().remoteAddress();
      checkoutUrl(URL, request.uri(), respStatus);
      checkoutMethod(request, respStatus);
      RequestEntity requestEntity = getContent(request, respStatus);
      System.out.println(requestEntity.toString());

      ResponseEntity responseEntity = new ResponseEntity("1.1.0", "I am server");
      responseEntity.setSuccess("success");
      ByteBuf bufRep = generateRspByteBuf(responseEntity, respStatus);
      writeResponse(request, respStatus, ctx, bufRep);
    } catch (Exception e) {
      ByteBuf bufRep = wrappedBuffer("error".getBytes("UTF-8"));
      writeResponse(request, respStatus, ctx, bufRep);
    } finally {
      msg.retain();
      ReferenceCountUtil.release(msg);
    }
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
   * @param request
   * @param responseStatus
   */
  private void checkoutMethod(FullHttpRequest request, HttpResponseStatus responseStatus)
      throws BasicException {
    HttpMethod method = request.method();
    if (POST.equals(method)) {
      checkoutContentType(getContentType(request), responseStatus);
    } else if (GET.equals(method)) {
      throw new BasicException("This method is GET!! ");
    } else {
      throw new BasicException("This method is other!! ");
    }
  }

  /**
   * Checkout Content-Type
   */
  private void checkoutContentType(List<String> contentType, HttpResponseStatus responseStatus)
      throws BasicException {
    if (!(contentType.contains(APPLICATION_JSON) || contentType.contains(TEXT_JSON))) {
      responseStatus = HttpResponseStatus.UNSUPPORTED_MEDIA_TYPE;
      throw new BasicException("http消息类型不支持");
    }
  }

  /**
   * 从请求中获取content-type，
   *
   * @param request http请求
   */
  private List<String> getContentType(FullHttpRequest request) {
    if (request.headers().contains(CONTENT_TYPE)) {
      List<String> contentTypeList = request.headers().getAll(CONTENT_TYPE);
      return contentTypeList;
    } else {
      return new ArrayList<String>();
    }
  }

  private RequestEntity getContent(FullHttpRequest request, HttpResponseStatus respStatus)
      throws BasicException {
    // 请求的content部分的bytes
    byte[] reqBytes;
    RequestEntity requestEntity = null;
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
      String reqContent = new String(reqBytes, "UTF-8");
      requestEntity = JSON.parseObject(reqContent,RequestEntity.class);
    } catch (UnsupportedEncodingException e) {
      respStatus = HttpResponseStatus.BAD_REQUEST;
      throw new BasicException("http消息解析失败");
    }

    return requestEntity;
  }

  /**
   * 生成返回响应
   */
  private ByteBuf generateRspByteBuf(ResponseEntity respEntity, HttpResponseStatus respStatus)
      throws BasicException {
    ByteBuf bufRep;
    try {
      bufRep = wrappedBuffer(JSON.toJSONBytes(respEntity));
    } catch (Exception e) {
      respStatus = HttpResponseStatus.INSUFFICIENT_STORAGE;
      throw new BasicException("http消息编码失败");
    }
    return bufRep;
  }

  private void writeResponse(FullHttpRequest request, HttpResponseStatus respStatus,
      ChannelHandlerContext ctx,
      ByteBuf bufRep) {
    if (request == null || bufRep == null) {
      ctx.close();
    } else {
      boolean keepAlive = HttpUtil.isKeepAlive(request);

      FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, respStatus, bufRep);

      if (respStatus == HttpResponseStatus.OK) {
        response.headers().set(CONTENT_TYPE, APPLICATION_JSON);
      } else {
        response.headers().set(CONTENT_TYPE, TEXT_PLAIN);
      }

      if (keepAlive) {
        //Add 'Content-Length' header only for a keep -alive connection
        response.headers().set(CONTENT_TYPE, response.content().readableBytes());
        // Add keep alive header as per:
        response.headers().set(CONNECTION, HttpHeaderValues.KEEP_ALIVE);
        ctx.write(response);
        ctx.flush();
      } else {
        ctx.write(response);
        ctx.flush();
      }
    }

  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    ctx.flush();
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    ctx.channel().close();
  }
}