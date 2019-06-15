package org.limmen.mystart;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.io.IOUtils;

public class GitHubPageLoader implements Enumeration<File> {
   
   static {
      TrustManager[] trustAllCerts = new TrustManager[]{
         new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
               return null;
            }

            @Override
            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
         }
      };

      // Activate the new trust manager
      try {
         SSLContext sc = SSLContext.getInstance("SSL");
         sc.init(null, trustAllCerts, new SecureRandom());
         HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
      }
      catch (Exception e) {
      }
   }

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
      try {
         URI uri = new URI(url);
         HttpURLConnection httpConn = (HttpURLConnection) uri.toURL().openConnection();
         int responseCode = httpConn.getResponseCode();

         // always check HTTP response code first
         if (responseCode == HttpURLConnection.HTTP_OK) {
            String fileName = "";
            String disposition = httpConn.getHeaderField("Content-Disposition");
            parseLinkHeader(httpConn.getHeaderField("Link"));

            if (disposition != null) {
               // extracts file name from header field
               int fileNameIndex = disposition.indexOf("filename=");
               if (fileNameIndex > 0) {
                  fileName = disposition.substring(fileNameIndex + 10, disposition.length() - 1);
               }
            } else {
               // extracts file name from URL
               int cutoff = url.indexOf('?') == -1 ? url.length() : url.indexOf('?');
               fileName = url.substring(url.lastIndexOf("/") + 1, cutoff);
            }

            // opens input stream from the HTTP connection
            try (InputStream inputStream = httpConn.getInputStream()) {
               return saveFile(inputStream, fileName);
            }
         }
         httpConn.disconnect();
      }
      catch (URISyntaxException ex) {
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
      }
      catch (IOException ex) {
         return null;
      }
   }
}
