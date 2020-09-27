package org.limmen.mystart.exporter;

import java.io.IOException;

public interface Exporter {

  String getName();

  String getDescription();

  void export(ExportContext context) throws IOException;
}
