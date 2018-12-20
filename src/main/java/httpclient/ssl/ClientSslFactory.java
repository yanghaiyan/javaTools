package httpclient.ssl;

import java.security.SecureRandom;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.ssl.SSLContextBuilder;

/**
 * 客户端安全验证
 */
public class ClientSslFactory {

  /**
   * 随机数种子.
   */
  private String seed = "wq8251jsdkfhntioy";

  /**
   * 安全随机数
   */
  private SecureRandom secureRandom = new SecureRandom(seed.getBytes());

  private static class Inner {

    static ClientSslFactory instance = new ClientSslFactory();
  }

  private SSLConnectionSocketFactory sslConnectionSocketFactory = null;

  private ClientSslFactory() {
    try {
      init();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static ClientSslFactory getInstance() {
    return Inner.instance;
  }

  private void init() throws Exception {
    SSLContextBuilder builder = new SSLContextBuilder();
    // 默认不需要随机数，特殊情况再加上.
    builder.setSecureRandom(null);
    // 全部信任，不对服务端的证书进行校验.
    builder.loadTrustMaterial((TrustStrategy) (x509Certificates, s) -> {
      for (int i = 0; i < x509Certificates.length; i++) {
        System.out.println("服务端的证书为： " + x509Certificates[i].toString());
      }
      return true;
    });

    /*对服务器端证书进行校验
     * .loadTrustMaterial(new File("D:\\truststore.jks"), "123456".toCharArray(), new TrustSelfSignedStrategy())
     *  发送客户端证书，服务端校验时会使用到
     * .loadKeyMaterial(keyStore, "123456".toCharArray())
     */

    sslConnectionSocketFactory = new SSLConnectionSocketFactory(builder.build(),
        new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null,
        NoopHostnameVerifier.INSTANCE);
  }

  /**
   * 获取ssl环境
   */
  public SSLConnectionSocketFactory getSslFac() {
    return sslConnectionSocketFactory;
  }
}
