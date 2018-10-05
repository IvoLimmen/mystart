package org.limmen.mystart.server.cleanup;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.URI;
import java.net.URL;
import java.net.UnknownHostException;
import java.net.http.HttpClient;
import java.net.http.HttpClient.Version;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import static java.time.temporal.ChronoUnit.SECONDS;
import java.util.concurrent.Callable;
import javax.net.ssl.SSLContext;
import lombok.extern.slf4j.Slf4j;
import org.limmen.mystart.Link;
import org.limmen.mystart.exception.StorageException;

@Slf4j
public class CleanupTask implements Callable<CleanupResult> {

  private final static SSLContext SSL_CONTEXT = SSLContextProvider.getSSLContext();

  private final Link link;
  private final CleanupContext context;

  public CleanupTask(final Link link, final CleanupContext context) {
    this.link = link;
    this.context = context;
  }

  @Override
  public CleanupResult call() throws Exception {
    String url = link.getUrl();

    if (url == null) {
      return new CleanupResult(link, "No URL", CleanupResultType.DELETE);
    } else if (link.isPrivateNetwork()) {
      return new CleanupResult(link, "Link is a private network", CleanupResultType.NOTHING);
    } else if (!url.startsWith("http")) {
      if (context.isAssumeHttps()) {
        url = "https://" + url;
      } else {
        url = "http://" + url;
      }
    }

    if (context.isMarkAsPrivateNetworkOnDomainError()) {
      CleanupResult cleanupResult = checkForReachableDomain(link);
      if (cleanupResult != null) {
        return cleanupResult;
      }
    }

    try {
      HttpClient client = HttpClient.newBuilder()
          .version(Version.HTTP_1_1)
          .followRedirects(HttpClient.Redirect.NEVER)
          .sslContext(SSL_CONTEXT)
          .connectTimeout(Duration.of(context.getMaximumTimeoutInSeconds(), SECONDS))
          .build();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(url))
          .GET()
          .build();

      HttpResponse<?> response = client.send(request, HttpResponse.BodyHandlers.discarding());

      switch (response.statusCode()) {
        case 200: // OK
          // we do nothing; it's ok
          return new CleanupResult(link, "200", CleanupResultType.NOTHING);
        case 301: // MOVED PERMANENTLY
        case 302: // MOVED TEMPORARELY
          String movedUrl = response.headers()
              .firstValue("Location")
              .orElse(null);
          if (movedUrl != null && movedUrl.length() > 0) {
            link.setUrl(movedUrl);
            return new CleanupResult(link, "Site has moved", CleanupResultType.UPDATE);
          }
          break;
        case 403: // FORBIDDEN
          return new CleanupResult(link, "Forbidden to access but exists", CleanupResultType.OK);
        case 404: // NOT FOUND
          return new CleanupResult(link, "Site no longer exists", CleanupResultType.DELETE);
        default:
          return new CleanupResult(link, "URL gave error code " + response.statusCode(), CleanupResultType.NOTHING);
      }
    } catch (MalformedURLException | NoRouteToHostException ex) {
      if (context.isMarkAsPrivateNetworkOnDomainError()) {
        link.setPrivateNetwork(true);
        return new CleanupResult(link, "URL is not valid! Marking private network.", CleanupResultType.UPDATE);
      } else {
        return new CleanupResult(link, "URL is not valid", CleanupResultType.DELETE);
      }
    } catch (IOException ex) {
      log.error(url, ex);
      return new CleanupResult(link, "Connection to the URL/Domain failed", CleanupResultType.NOTHING);
    }

    return new CleanupResult(link, "?", CleanupResultType.NOTHING);
  }

  private CleanupResult checkForReachableDomain(Link link) {
    try {
      InetAddress address = InetAddress.getByName(new URL(link.getUrl()).getHost());
      if (address.isSiteLocalAddress()) {
        link.setPrivateNetwork(true);
        return new CleanupResult(link, "Host/IP address is a site local address. Marking private network.", CleanupResultType.UPDATE);
      }
    } catch (MalformedURLException | UnknownHostException | StorageException ex) {
      // ignore
    }
    return null;
  }
}
