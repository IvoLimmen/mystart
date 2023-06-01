package org.limmen.mystart.cleanup;

import static java.time.temporal.ChronoUnit.SECONDS;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import javax.net.ssl.SSLContext;

import org.limmen.mystart.Link;
import org.limmen.mystart.util.SSLContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CleanupTaskJdk11 extends AbstractCleanupTask {

  private final static Logger log = LoggerFactory.getLogger(CleanupTaskJdk11.class);
  
  private final static SSLContext SSL_CONTEXT = SSLContextProvider.getSSLContext();

  public CleanupTaskJdk11(final Link link, final CleanupContext context) {
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
      HttpClient client = HttpClient.newBuilder()
          .version(Version.HTTP_1_1)
          .followRedirects(HttpClient.Redirect.NEVER)
          .sslContext(SSL_CONTEXT)
          .connectTimeout(Duration.of(getContext().getMaximumTimeoutInSeconds(), SECONDS))
          .build();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(url))
          .GET()
          .build();

      HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.discarding());

      return determineResult(
          response.statusCode(),
          response.headers().firstValue("Location").orElse(null));
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
