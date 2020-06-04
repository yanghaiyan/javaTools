package helper;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.cert.CertPath;
import java.security.cert.CertPathValidator;
import java.security.cert.CertPathValidatorException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.PKIXCertPathValidatorResult;
import java.security.cert.PKIXParameters;
import java.security.cert.TrustAnchor;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;

/**
 * 根据url获取证书链
 */
public class VerifyCerChainHelper {

  public static ArrayList<Certificate> getCertList(String serverUrl) {
    ArrayList<Certificate> cerList = new ArrayList<Certificate>();
    try {
      URL url = new URL(serverUrl);
      HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
      conn.connect();

      Certificate[] certs = conn.getServerCertificates(); // 会拿到完整的证书链

      conn.disconnect();

      for (int i = 0; i < certs.length; i++) {
        cerList.add(certs[i]);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return cerList;
  }

  /**
   * 验证证书链 验证证书链
   */
  public static void VerifyCerList(List<? extends Certificate> cerList, String trustAnchor) {

    try {

      CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
      CertPathValidator cpv = CertPathValidator.getInstance("PKIX");
      //
      CertPath cp = certificateFactory.generateCertPath(cerList);

      // 设置锚点
      FileInputStream input = new FileInputStream(trustAnchor);
      X509Certificate trust = (X509Certificate) certificateFactory.generateCertificate(input);
      // Create TrustAnchor
      TrustAnchor anchor = new TrustAnchor(trust, null);

      // Set the PKIX parameters
      PKIXParameters params = new PKIXParameters(Collections.singleton(anchor));
      params.setRevocationEnabled(false);
      PKIXCertPathValidatorResult result = (PKIXCertPathValidatorResult) cpv.validate(cp, params);

      if (result != null) {
        System.out.println("success");
        System.out.println(result);
      }
    } catch (CertPathValidatorException cpve) {
      System.out.println("Validation failure, cert[" + cpve.getIndex() + "] :" + cpve.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  private static String getThumbPrint(X509Certificate cert) throws Exception {
    MessageDigest md = MessageDigest.getInstance("SHA-1");
    byte[] der = cert.getEncoded();
    md.update(der);
    byte[] digest = md.digest();
    return bytesToHexString(digest);
  }

  private static String bytesToHexString(byte[] src) {
    StringBuilder stringBuilder = new StringBuilder("");
    if (src == null || src.length <= 0) {
      return null;
    }
    for (int i = 0; i < src.length; i++) {
      int v = src[i] & 0xFF;
      String hv = Integer.toHexString(v);
      if (hv.length() < 2) {
        stringBuilder.append(0);
      }
      stringBuilder.append(hv);
    }

    return stringBuilder.toString();
  }
}