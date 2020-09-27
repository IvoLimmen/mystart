package org.limmen.mystart.exporter;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class ExportContext {

  private final List<String> labels = new ArrayList<>();

  public ExportContext() {    
  }

  public void addLabel(String label) {    
    this.labels.add(label);
  }  
}
