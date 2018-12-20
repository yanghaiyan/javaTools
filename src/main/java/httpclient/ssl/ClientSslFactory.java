package httpclient.ssl;

import java.security.SecureRandom;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.ssl.SSLContextBuilder;

/**
 * �ͻ��˰�ȫ��֤
 */
public class ClientSslFactory {

  /**
   * ���������.
   */
  private String seed = "wq8251jsdkfhntioy";

  /**
   * ��ȫ�����
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
    // Ĭ�ϲ���Ҫ���������������ټ���.
    builder.setSecureRandom(null);
    // ȫ�����Σ����Է���˵�֤�����У��.
    builder.loadTrustMaterial((TrustStrategy) (x509Certificates, s) -> {
      for (int i = 0; i < x509Certificates.length; i++) {
        System.out.println("����˵�֤��Ϊ�� " + x509Certificates[i].toString());
      }
      return true;
    });

    /*�Է�������֤�����У��
     * .loadTrustMaterial(new File("D:\\truststore.jks"), "123456".toCharArray(), new TrustSelfSignedStrategy())
     *  ���Ϳͻ���֤�飬�����У��ʱ��ʹ�õ�
     * .loadKeyMaterial(keyStore, "123456".toCharArray())
     */

    sslConnectionSocketFactory = new SSLConnectionSocketFactory(builder.build(),
        new String[]{"SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2"}, null,
        NoopHostnameVerifier.INSTANCE);
  }

  /**
   * ��ȡssl����
   */
  public SSLConnectionSocketFactory getSslFac() {
    return sslConnectionSocketFactory;
  }
}
