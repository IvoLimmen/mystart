package org.limmen.mystart.importer;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;

@Getter
public class ParseContext {

  private final String fileName;

  private final ByteArrayInputStream inputStream;

  private final Map<String, String> options = new HashMap<>();

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

  public void addOption(String key, String value) {
    this.options.put(key, value);
  }

  public InputStream getInputStream() {
    inputStream.reset();
    return inputStream;
  }

  public String getOption(String key) {
    return this.options.get(key);
  }

  public boolean hasData() {
    return this.inputStream != null;
  }

  public boolean hasOption(String key) {
    return this.options.containsKey(key);
  }
}
