package org.limmen.mystart.cleanup;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import lombok.Getter;
import org.limmen.mystart.Link;
import org.limmen.mystart.exception.StorageException;

@Getter
public abstract class AbstractCleanupTask implements CleanupTask {

  private final CleanupContext context;
  private final Link link;

  public AbstractCleanupTask(Link link, CleanupContext context) {
    this.link = link;
    this.context = context;
  }

  protected CleanupResult determineResult(int statusCode, String movedUrl) {
    switch (statusCode) {
      case 200: // OK
        // we do nothing; it's ok
        return new CleanupResult(getLink(), "200", CleanupResultType.NOTHING);
      case 301: // MOVED PERMANENTLY
      case 302: // MOVED TEMPORARELY
        if (movedUrl != null && movedUrl.length() > 0) {
          getLink().setUrl(movedUrl);
        }
        return new CleanupResult(getLink(), "Site has moved", CleanupResultType.UPDATE);
      case 403: // FORBIDDEN
        return new CleanupResult(getLink(), "Forbidden to access but exists", CleanupResultType.NOTHING);
      case 404: // NOT FOUND
        return new CleanupResult(getLink(), "Site no longer exists", CleanupResultType.DELETE);
      default:
        return new CleanupResult(getLink(), "URL gave error code " + statusCode, CleanupResultType.NOTHING);
    }
  }

  protected CleanupResult checkForReachableDomain(Link link) {
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
