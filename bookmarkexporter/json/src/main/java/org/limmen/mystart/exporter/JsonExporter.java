package org.limmen.mystart.exporter;

import java.io.IOException;

public class JsonExporter implements Exporter {

  @Override
  public void export(ExportContext ec) throws IOException {
  }

  @Override
  public String getDescription() {
    return "Export in JSON file format.";
  }

  @Override
  public String getName() {
    return "JSON";
  }  
}
