package org.limmen.mystart.cleanup;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.util.concurrent.TimeUnit;
import javax.net.ssl.SSLContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.limmen.mystart.Link;

@Slf4j
public class CleanupTaskJdk8 extends AbstractCleanupTask {

  private final static SSLContext SSL_CONTEXT = SSLContextProvider.getSSLContext();

  public CleanupTaskJdk8(final Link link, final CleanupContext context) {
    super(link, context);
  }

  @Override
  public CleanupResult call() throws Exception {
    String url = getLink().getUrl();

    if (url == null) {
      return new CleanupResult(getLink(), "No URL", CleanupResultType.DELETE);
    } else if (getLink().isPrivateNetwork()) {
      return new CleanupResult(getLink(), "Link is a private network", CleanupResultType.NOTHING);
    } else if (!url.startsWith("http")) {
      if (getContext().isAssumeHttps()) {
        url = "https://" + url;
      } else {
        url = "http://" + url;
      }
    }

    if (getContext().isMarkAsPrivateNetworkOnDomainError()) {
      CleanupResult cleanupResult = checkForReachableDomain(getLink());
      if (cleanupResult != null) {
        return cleanupResult;
      }
    }

    try {
      final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
          SSL_CONTEXT,
          new String[]{"TLSv1.1", "TLSv1.2"},
          null,
          SSLConnectionSocketFactory.getDefaultHostnameVerifier());

      try (CloseableHttpClient httpclient = HttpClients.custom()
          .setConnectionTimeToLive(getContext().getMaximumTimeoutInSeconds(), TimeUnit.SECONDS)
          .setSSLSocketFactory(sslsf)
          .build()) {

        final HttpGet httpget = new HttpGet(url);
        final HttpClientContext clientContext = HttpClientContext.create();

        try (CloseableHttpResponse response = httpclient.execute(httpget, clientContext)) {
          return determineResult(
              response.getStatusLine().getStatusCode(),
              response.getFirstHeader("Location").getValue());
        }
      }
    } catch (MalformedURLException | NoRouteToHostException ex) {
      if (getContext().isMarkAsPrivateNetworkOnDomainError()) {
        getLink().setPrivateNetwork(true);
        return new CleanupResult(getLink(), "URL is not valid! Marking private network.", CleanupResultType.UPDATE);
      } else {
        return new CleanupResult(getLink(), "URL is not valid", CleanupResultType.DELETE);
      }
    } catch (IOException ex) {
      log.error(url, ex);
      return new CleanupResult(getLink(), "Connection to the URL/Domain failed", CleanupResultType.NOTHING);
    }
  }
}
