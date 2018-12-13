package netty;

import static org.apache.http.HttpHeaders.CONTENT_TYPE;

import com.alibaba.fastjson.JSON;
import exception.BasicException;
import java.io.IOException;
import netty.entity.RequestEntity;
import netty.entity.ResponseEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class HttpClientServer {

  private static final String APPLICATION_JSON = "application/json";
  private CloseableHttpClient httpClient = null;
  private CloseableHttpResponse response = null;
  private ResponseEntity responseEntity = null;

  public HttpClientServer() {
    init();
  }

  /**
   * ��ʼ��
   */
  private void init() {
    httpClient = HttpClients.createDefault();
  }

  public ResponseEntity doPost(String host, int port, String jsonStr) throws BasicException {
    String url = "http://" + host + ":" + port;
    HttpPost httpPost = new HttpPost(url);
    try {
      StringEntity entity = new StringEntity(jsonStr);
      entity.setContentEncoding("UTF-8");
      entity.setContentType(APPLICATION_JSON);
      httpPost.setEntity(entity);
      httpPost.setHeader(CONTENT_TYPE, APPLICATION_JSON);
      response = httpClient.execute(httpPost);

      String result = EntityUtils.toString(response.getEntity());
      responseEntity = JSON.parseObject(result, ResponseEntity.class);

    } catch (Exception e) {
      throw new BasicException("��������ʧ��", e);
    }

    return responseEntity;
  }

  public ResponseEntity doGet(String host, int port, String jsonStr) throws BasicException {
    String url = "http://" + host + ":" + port + "/get";
    HttpGet httpGet = new HttpGet(url);
    try {
      response = httpClient.execute(httpGet);
      String result = EntityUtils.toString(response.getEntity());
      responseEntity = JSON.parseObject(result, ResponseEntity.class);
    } catch (ClientProtocolException e) {
      throw new BasicException("��������ʧ��", e);
    } catch (IOException e) {
      throw new BasicException("�������ݽ���ʧ��", e);
    }
    return responseEntity;
  }

  public static void main(String[] args) {
    HttpClientServer clientServer = new HttpClientServer();
    RequestEntity request = new RequestEntity("hello","1.0");
    try {
      ResponseEntity response = clientServer.doPost("127.0.0.1",8099,JSON.toJSONString(request));
      System.out.println(JSON.toJSONString(response));
    } catch (BasicException e) {
      e.printStackTrace();
    }
  }
}
