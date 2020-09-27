package org.limmen.mystart.importer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import static java.time.temporal.ChronoUnit.SECONDS;
import java.util.Enumeration;
import java.util.Optional;
import javax.net.ssl.SSLContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.limmen.mystart.util.SSLContextProvider;

@Slf4j
public class GitHubPageLoader implements Enumeration<File> {

  private final static SSLContext SSL_CONTEXT = SSLContextProvider.getSSLContext();

  private final static String USER_AGENT = "MyStart";

  private String nextUrl = null;

  private int pageIndex = 0;

  private int lastPage = -1;

  public GitHubPageLoader() {
  }

  private void parseLinkHeader(String header) {
    if (header == null || header.length() == 0) {
      return;
    }

    if (lastPage != -1) {
      return;
    }
    String[] links = header.split(",");

    String next = links[0].trim();
    String last = links[1].trim();

    String lastPageStr = last.substring(last.indexOf("?page=") + 6);

    this.lastPage = Integer.parseInt(lastPageStr.substring(0, lastPageStr.indexOf('>')));
    this.nextUrl = next.substring(1, next.indexOf("?page="));
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
        parseLinkHeader(response.headers().firstValue("Link").get());

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
    return this.pageIndex < this.lastPage;
  }

  @Override
  public File nextElement() {
    try {
      return downloadFile(nextUrl + "?page=" + pageIndex++);
    } catch (IOException ex) {
      return null;
    }
  }
}
