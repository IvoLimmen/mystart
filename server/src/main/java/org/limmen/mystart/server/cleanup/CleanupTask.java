package org.limmen.mystart.server.cleanup;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.limmen.mystart.Link;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.exception.StorageException;

@Slf4j
public class CleanupTask implements Runnable {

  private final CleanupContext context;
  private final LinkStorage linkStorage;

  public CleanupTask(final LinkStorage linkStorage, final CleanupContext context) {
    this.linkStorage = linkStorage;
    this.context = context;
  }

  @Override
  public void run() {
    try {
      Collection<Link> links = this.linkStorage.getAll(context.getUserId());
      for (Link link : links) {

        String url = link.getUrl();

        if (url == null) {
          log.error("NO URL!!! id {}", link.getId());
          this.linkStorage.remove(context.getUserId(), link.getId());
          continue;
        } else if (link.isPrivateNetwork()) {
          log.info("{} is a private network, skipping", link.getUrl());
          continue;
        } else if (!url.startsWith("http")) {
          if (context.isAssumeHttps()) {
            url = "https://" + url;
          } else {
            url = "http://" + url;
          }
        }

        if (context.isMarkAsPrivateNetworkOnDomainError()) {
          if (checkForReachableDomain(link)) {
            continue;
          }
        }

        try {
          HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
          connection.setConnectTimeout(5000);
          switch (connection.getResponseCode()) {
            case 200: // OK
              // we do nothing; it's ok
              log.info("{} is ok", link.getUrl());
              break;
            case 301: // MOVED PERMANENTLY
            case 302: // MOVED TEMPORARELY
              String movedUrl = connection.getHeaderField("Location");
              if (movedUrl != null && movedUrl.length() > 0) {
                this.linkStorage.remove(context.getUserId(), link.getId());
                link.setUrl(movedUrl);
                log.info("{} is moved to {}, updated", url, movedUrl);
                this.linkStorage.update(context.getUserId(), link);
              }
              break;
            case 404: // NOT FOUND
              log.warn("URL {} no longer exists", link.getUrl());
              this.linkStorage.remove(context.getUserId(), link.getId());
              break;
            default:
              log.warn("URL {} gave error code {}", link.getUrl(), connection.getResponseCode());
              break;
          }
        } catch (MalformedURLException | NoRouteToHostException ex) {
          if (context.isMarkAsPrivateNetworkOnDomainError()) {
            log.error("URL is not valid! Marking private network. {}", link.getUrl());
            link.setPrivateNetwork(true);
            this.linkStorage.update(context.getUserId(), link);
          } else {
            log.error("URL is not valid! Removing. {}", link.getUrl());
            this.linkStorage.remove(context.getUserId(), link.getId());
          }
        } catch (IOException ex) {
          log.error("Connection to the URL/Domain failed {}", link.getUrl());
          this.linkStorage.remove(context.getUserId(), link.getId());
        }
      }
    } catch (StorageException ex) {
      log.error("Error while retieving all links for cleanup task...", ex);
    }
  }

  private boolean checkForReachableDomain(Link link) {
    try {
      InetAddress address = InetAddress.getByName(new URL(link.getUrl()).getHost());
      if (address.isSiteLocalAddress()) {
        log.error("Host/IP address is a site local address. Marking private network. {}", link.getUrl());
        link.setPrivateNetwork(true);
        this.linkStorage.update(context.getUserId(), link);
        return true;
      }
    } catch (MalformedURLException | UnknownHostException | StorageException ex) {
      // ignore
    }
    return false;
  }
}
