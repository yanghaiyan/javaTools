package httpclient;

import static org.apache.http.HttpHeaders.CONTENT_TYPE;

import httpclient.ssl.ClientSslFactory;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Map;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;


/**
 * ���ӳ�
 *
 * @author
 */
public class PoolHttpClient {

  // �������������
  private static final int DEFAULT_POOL_MAX_TOTAL = 50;
  // ���·����������
  private static final int DEFAULT_POOL_MAX_PER_ROUTE = 200;

  // Ĭ�����ӳ�ʱʱ��
  private static final int DEFAULT_CONNECT_TIMEOUT = 10 * 1000;
  // Ĭ�ϴ����ӳػ�ȡ���ӳ�ʱʱ��
  private static final int DEFAULT_CONNECT_REQUEST_TIMEOUT = 10 * 1000;
  // Ĭ��TCP���� ��ʱʱ��
  private static final int DEFAULT_SOCKET_TIMEOUT = 10 * 1000;
  private static final String APPLICATION_JSON = "application/json";

  // ���ӳع�����
  private PoolingHttpClientConnectionManager gcm = null;

  private CloseableHttpClient httpClient = null;

  // ���������߳�
  private IdleConnectionMonitorThread idleThread = null;

  // ���ӳص����������
  private final int maxTotal;
  // ���ӳذ�route���õ����������
  private final int maxPerRoute;

  // tcp connect�ĳ�ʱʱ��
  private final int connectTimeout;
  // �����ӳػ�ȡ���ӵĳ�ʱʱ��
  private final int connectRequestTimeout;
  // tcp io�Ķ�д��ʱʱ��
  private final int socketTimeout;


  public PoolHttpClient() {
    this(
        PoolHttpClient.DEFAULT_POOL_MAX_TOTAL,
        PoolHttpClient.DEFAULT_POOL_MAX_PER_ROUTE,
        PoolHttpClient.DEFAULT_CONNECT_TIMEOUT,
        PoolHttpClient.DEFAULT_CONNECT_REQUEST_TIMEOUT,
        PoolHttpClient.DEFAULT_SOCKET_TIMEOUT
    );
  }

  public PoolHttpClient(int maxTotal, int maxPerRoute, int connectTimeout,
      int connectRequestTimeout, int socketTimeout) {
    this.maxTotal = maxTotal;
    this.maxPerRoute = maxPerRoute;
    this.connectTimeout = connectTimeout;
    this.connectRequestTimeout = connectRequestTimeout;
    this.socketTimeout = socketTimeout;

    Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
        .register("http", PlainConnectionSocketFactory.getSocketFactory())
        .register("https", ClientSslFactory.getInstance().getSslFac())
        .build();

    this.gcm = new PoolingHttpClientConnectionManager(registry);
    this.gcm.setMaxTotal(maxTotal);
    this.gcm.setDefaultMaxPerRoute(maxPerRoute);

    RequestConfig requestConfig = RequestConfig.custom()
        // �������ӳ�ʱ
        .setConnectTimeout(this.connectTimeout)
        // ���ö�ȡ��ʱ
        .setSocketTimeout(this.socketTimeout)
        // ���ô����ӳػ�ȡ����ʵ���ĳ�ʱ
        .setConnectionRequestTimeout(this.connectRequestTimeout)
        .build();

    HttpClientBuilder builder = HttpClients.custom();
    httpClient = builder
        /**  ע�����ӹ���*/
        .setConnectionManager(this.gcm)
        .setDefaultRequestConfig(requestConfig)
        .build();

    idleThread = new IdleConnectionMonitorThread(this.gcm);
    idleThread.start();
  }

  public String doGet(String url) {
    return this.doGet(url, Collections.EMPTY_MAP, Collections.EMPTY_MAP);
  }

  public String doGet(String url, Map<String, Object> params) {
    return this.doGet(url, Collections.EMPTY_MAP, params);
  }

  public String doGet(String url, Map<String, String> headers, Map<String, Object> params) {

    /** ����GET����ͷ.*/
    String apiUrl = getUrlWithParams(url, params);
    HttpGet httpGet = new HttpGet(apiUrl);
    httpGet.setHeader("User-Agent",
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.62 Safari/537.36");

    /** ����header��Ϣ.*/
    if (headers != null && headers.size() > 0) {
      for (Map.Entry<String, String> entry : headers.entrySet()) {
        httpGet.addHeader(entry.getKey(), entry.getValue());
      }
    }

    return handlerResp(httpGet);
  }

  /**
   * ����post ����
   *
   * @param apiUrl url��ַ
   * @param content content����
   * @return ��Ӧ����
   */
  public String doPost(String apiUrl, String content) {

    HttpPost httpPost = new HttpPost(apiUrl);

    httpPost.setHeader("User-Agent",
        "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/69.0.3497.100 Safari/537.36");
    httpPost.setHeader(CONTENT_TYPE, APPLICATION_JSON);
    HttpEntity entityReq = new StringEntity(content, Charset.forName("utf-8"));
    ((StringEntity) entityReq).setContentType(APPLICATION_JSON);
    httpPost.setEntity(entityReq);
    return handlerResp(httpPost);
  }


  /**
   * ������Ӧ
   */
  private String handlerResp(HttpUriRequest request) {
    CloseableHttpResponse response = null;
    try {
      response = httpClient.execute(request);
      if (response == null || response.getStatusLine() == null) {
        return null;
      }

      int statusCode = response.getStatusLine().getStatusCode();
      if (statusCode == HttpStatus.SC_OK) {
        HttpEntity entityRes = response.getEntity();
        if (entityRes != null) {
          String content = EntityUtils.toString(entityRes, "UTF-8");
          return content;
        }
      }
      return null;
    } catch (IOException e) {
    } finally {
      if (response != null) {
        try {
          response.close();
        } catch (IOException e) {
        }
      }
    }
    return null;
  }

  private String getUrlWithParams(String url, Map<String, Object> params) {
    boolean first = true;
    StringBuilder sb = new StringBuilder(url);
    for (String key : params.keySet()) {
      char ch = '&';
      if (first == true) {
        ch = '?';
        first = false;
      }
      String value = params.get(key).toString();
      try {
        String sval = URLEncoder.encode(value, "UTF-8");
        sb.append(ch).append(key).append("=").append(sval);
      } catch (UnsupportedEncodingException e) {
      }
    }
    return sb.toString();
  }
}
