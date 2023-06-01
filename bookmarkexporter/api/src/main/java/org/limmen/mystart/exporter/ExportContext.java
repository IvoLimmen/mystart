package org.limmen.mystart.exporter;

import java.util.ArrayList;
import java.util.List;

public class ExportContext {

  private final List<String> labels = new ArrayList<>();

  public ExportContext() {    
  }

  public void addLabel(String label) {    
    this.labels.add(label);
  }

  public List<String> getLabels() {
    return labels;
  }
}
