package org.limmen.mystart.cleanup;

import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class SSLContextProvider {

  private static SSLContext sslContext;

  public static SSLContext getSSLContext() {
    if (sslContext == null) {
      TrustManager[] certs = new TrustManager[]{
        new X509TrustManager() {
          @Override
          public X509Certificate[] getAcceptedIssuers() {
            return null;
          }

          @Override
          public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
          }

          @Override
          public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
          }
        }
      };
      try {
        sslContext = SSLContext.getInstance("SSL");
        sslContext.init(null, certs, new SecureRandom());
      } catch (Exception e) {
        // ignore exception
      }
    }
    return sslContext;
  }
}
