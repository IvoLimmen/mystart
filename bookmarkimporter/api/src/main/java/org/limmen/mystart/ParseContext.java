package org.limmen.mystart;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class ParseContext {

   private final String fileName;

   private final ByteArrayInputStream inputStream;

   private final String temporaryFileName;

   private final String url;

   public ParseContext(final String url) {
      this.url = url;
      this.inputStream = null;
      this.fileName = null;
      this.temporaryFileName = null;
   }

   public ParseContext(final ByteArrayInputStream inputStream, final String temporaryFileName, final String fileName) {
      this.url = null;
      this.inputStream = inputStream;
      this.temporaryFileName = temporaryFileName;
      this.fileName = fileName;
      if (this.inputStream != null) {
         this.inputStream.mark(0);
      }
   }

   public String getFileName() {
      return fileName;
   }

   public InputStream getInputStream() {
      inputStream.reset();
      return inputStream;
   }

   public String getTemporaryFileName() {
      return temporaryFileName;
   }

   public String getUrl() {
      return url;
   }

   public boolean hasData() {
      return this.inputStream != null;
   }
}
