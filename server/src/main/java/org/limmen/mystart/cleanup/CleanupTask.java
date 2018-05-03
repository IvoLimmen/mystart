package org.limmen.mystart.cleanup;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collection;
import org.limmen.mystart.Link;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.User;
import org.limmen.mystart.UserStorage;
import org.limmen.mystart.exception.StorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CleanupTask implements Runnable {

   private static final Logger LOGGER = LoggerFactory.getLogger(CleanupTask.class);

   private final UserStorage userStorage;

   private final LinkStorage linkStorage;

   private final CleanupContext context;

   public CleanupTask(final UserStorage userStorage, final LinkStorage linkStorage, final CleanupContext context) {
      this.linkStorage = linkStorage;
      this.userStorage = userStorage;
      this.context = context;
   }

   @Override
   public void run() {
      try {
         User user = this.userStorage.get(context.getUserId());

         Collection<Link> links = null;
         if (context.getUrl() != null) {
            links = new ArrayList<>();
            Link link = new Link(context.getUrl());
            links.add(this.linkStorage.get(user.getId(), link.getId()));
         } else {
            links = this.linkStorage.getAll(user.getId());
         }
         for (Link link : links) {

            String url = link.getUrl();

            if (url == null) {
               LOGGER.error("NO URL!!! id {}", link.getId());
               this.linkStorage.remove(user.getId(), link.getId());
               continue;
            } else if (link.isPrivateNetwork()) {
               LOGGER.info("{} is a private network, skipping", link.getUrl());
               continue;
            } else if (!url.startsWith("http")) {
               url = "http://" + url;
            }

            if (context.markAsPrivateNetworkOnDomainError()) {
               try {
                  InetAddress address = InetAddress.getByName(link.getHost());
                  if (address.isSiteLocalAddress()) {
                     LOGGER.error("Host/IP address is a site local address. Marking private network. {}", link.getUrl());
                     link.setPrivateNetwork(true);
                     this.linkStorage.store(user.getId(), link);
                     continue;
                  }
               }
               catch (UnknownHostException ex) {
                  // ignore
               }
            }

            try {
               HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
               connection.setConnectTimeout(5000);
               switch (connection.getResponseCode()) {
                  case 200: // OK
                     // we do nothing; it's ok
                     LOGGER.info("{} is ok", link.getUrl());
                     break;
                  case 301: // MOVED PERMANENTLY
                  case 302: // MOVED TEMPORARELY
                     String movedUrl = connection.getHeaderField("Location");
                     if (movedUrl != null && movedUrl.length() > 0) {
                        this.linkStorage.remove(user.getId(), link.getId());
                        link.setUrl(movedUrl);
                        LOGGER.info("{} is moved to {}, updated", url, movedUrl);
                        this.linkStorage.store(user.getId(), link);
                     }
                     break;
                  case 404: // NOT FOUND
                     LOGGER.warn("URL {} no longer exists", link.getUrl());
                     this.linkStorage.remove(user.getId(), link.getId());
                     break;
                  default:
                     LOGGER.warn("URL {} gave error code {}", link.getUrl(), connection.getResponseCode());
                     break;
               }
            }
            catch (MalformedURLException | NoRouteToHostException ex) {
               if (context.markAsPrivateNetworkOnDomainError()) {
                  LOGGER.error("URL is not valid! Marking private network. {}", link.getUrl());
                  link.setPrivateNetwork(true);
                  this.linkStorage.store(user.getId(), link);
               } else {
                  LOGGER.error("URL is not valid! Removing. {}", link.getUrl());
                  this.linkStorage.remove(user.getId(), link.getId());
               }
            }
            catch (IOException ex) {
               LOGGER.error("Connection to the URL/Domain failed {}", link.getUrl());
               this.linkStorage.remove(user.getId(), link.getId());
            }
         }
      }
      catch (StorageException ex) {
         LOGGER.error("Error while retieving all links for cleanup task...", ex);
      }
   }
}
