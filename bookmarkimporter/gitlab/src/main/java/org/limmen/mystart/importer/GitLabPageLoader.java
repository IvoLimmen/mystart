package org.limmen.mystart.importer;

import static java.time.temporal.ChronoUnit.SECONDS;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Enumeration;
import java.util.Optional;

import javax.net.ssl.SSLContext;

import org.apache.commons.io.IOUtils;
import org.limmen.mystart.util.SSLContextProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GitLabPageLoader implements Enumeration<File> {

  private final static Logger log = LoggerFactory.getLogger(GitLabPageLoader.class);
  
  private final static SSLContext SSL_CONTEXT = SSLContextProvider.getSSLContext();

  private final static String USER_AGENT = "MyStart";

  private String nextUrl = null;

  public GitLabPageLoader() {
  }

  private void parseLinkHeader(String header) {
    if (header == null || header.length() == 0) {
      return;
    }

    this.nextUrl = null;
    String[] links = header.split(",");
    for (String link: links) {
      String[] parts = link.trim().split(";");
      String url = parts[0].trim();
      String type = parts[1].trim();
      
      if (type.equalsIgnoreCase("rel=\"next\"")) {
        this.nextUrl = url;
      }
    }    
  }

  public File downloadFile(String url) throws IOException {
    log.info("Downloading next page ({})", url);

    try {
      HttpClient client = HttpClient.newBuilder()
          .followRedirects(HttpClient.Redirect.NEVER)
          .sslContext(SSL_CONTEXT)
          .connectTimeout(Duration.of(30, SECONDS))
          .build();
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(url))
          .header("User-Agent", USER_AGENT)
          .GET()
          .build();

      HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());

      int responseCode = response.statusCode();

      // always check HTTP response code first
      if (responseCode == 200) {
        String fileName = "";
        Optional<String> disposition = response.headers().firstValue("Content-Disposition");
        parseLinkHeader(response.headers().firstValue("link").get());

        if (disposition.isPresent()) {
          // extracts file name from header field
          int fileNameIndex = disposition.get().indexOf("filename=");
          if (fileNameIndex > 0) {
            fileName = disposition.get().substring(fileNameIndex + 10, disposition.get().length() - 1);
          }
        } else {
          // extracts file name from URL
          int cutoff = url.indexOf('?') == -1 ? url.length() : url.indexOf('?');
          fileName = url.substring(url.lastIndexOf("/") + 1, cutoff);
        }

        // opens input stream from the HTTP connection
        try (InputStream inputStream = response.body()) {
          return saveFile(inputStream, fileName);
        }
      } else if (responseCode >= 400 && responseCode <= 499) {
        log.warn("Not authorized/Rate limit exceeded");
        return null;
      }
    } catch (IOException | InterruptedException ex) {
      //
    }
    return null;
  }

  private File saveFile(InputStream inputStream, String fileName) throws IOException {
    File tmpFile = File.createTempFile("mystart", fileName);
    try (FileOutputStream outputStream = new FileOutputStream(tmpFile)) {
      IOUtils.copy(inputStream, outputStream);
    }
    inputStream.close();
    return tmpFile;
  }

  @Override
  public boolean hasMoreElements() {
    return this.nextUrl != null;
  }

  @Override
  public File nextElement() {
    try {
      return downloadFile(nextUrl);
    } catch (IOException ex) {
      return null;
    }
  }
}
