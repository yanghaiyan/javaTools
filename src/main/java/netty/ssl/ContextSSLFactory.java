package netty.ssl;

import helper.FileHelper;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.SecureRandom;
import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextSSLFactory {

  private Logger logger = LoggerFactory.getLogger(this.getClass());
  /**
   * keystore path.
   */
  private static String KEY_STORE_PATH = "/keystore/keystore2.jks";
  /**
   * keystore password
   */
  private static String KEYSTORE_PWD = "123456";
  /**
   * SSL Version.
   */
  private static String sslVersion = "TLS";

  /**
   * 随机种子
   */
  private final String SEED = "wq8251jsdkfhntioy";

  /**
   * 安全随机数.
   */
  private SecureRandom secureRandom = new SecureRandom(SEED.getBytes());
  /**
   * ssl 环境.
   */
  private SSLEngine sslEngine = null;

  private static class Inner {

    static ContextSSLFactory instance = new ContextSSLFactory();
  }

  public static ContextSSLFactory getInstance() {
    return Inner.instance;
  }


  public SSLEngine getSslEngine() {
    return sslEngine;
  }

  private ContextSSLFactory() {
    init();
  }

  /***
   * Init
   */
  private void init() {
    try {
      KeyStore keyStore = getKeyStore();
      TrustManager[] trustManagers = getTrustManagers(keyStore);
      KeyManager[] keyManagers = getKeyManagersServer(keyStore);

      SSLContext sslContext = SSLContext.getInstance(sslVersion);
      sslContext.init(keyManagers, trustManagers, null);
      sslContext.createSSLEngine().getSupportedCipherSuites();

      sslEngine = sslContext.createSSLEngine();
      sslEngine.setUseClientMode(false);
      sslEngine.setNeedClientAuth(false);
    } catch (Exception e) {
      logger.error("Init SSLEngine failed！！！", e);
    }
  }


  /**
   * 获取 keystore
   */
  private KeyStore getKeyStore() {
    FileInputStream inputStream = null;
    KeyStore keyStore = null;

    try {

      inputStream = new FileInputStream(
          new File(FileHelper.getFilePath(this.getClass()) + KEY_STORE_PATH));
      keyStore = KeyStore.getInstance("JKS");
      keyStore.load(inputStream, KEYSTORE_PWD.toCharArray());
    } catch (Exception e) {
      logger.error("Gets the keystore failed", e);
    } finally {
      if (inputStream != null) {
        try {
          inputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }

    return keyStore;
  }

  /**
   * 获取信任的锚点
   */
  private TrustManager[] getTrustManagers(KeyStore keyStore) {
    TrustManager[] kms = null;

    try {
      TrustManagerFactory keyFactory = TrustManagerFactory.getInstance("SunX509");
      keyFactory.init(keyStore);
      kms = keyFactory.getTrustManagers();
    } catch (Exception e) {
      logger.error("Gets the TrustManager error!", e);
    }

    return kms;
  }

  /**
   *
   * @param keyStore
   * @return
   */
  private KeyManager[] getKeyManagersServer(KeyStore keyStore) {
    KeyManager[] kms = null;

    try {
      KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
      keyManagerFactory.init(keyStore, KEYSTORE_PWD.toCharArray());
      kms = keyManagerFactory.getKeyManagers();
    } catch (Exception e) {
      logger.error("获取服务端信任参数失败", e);
    }

    return kms;
  }
}
