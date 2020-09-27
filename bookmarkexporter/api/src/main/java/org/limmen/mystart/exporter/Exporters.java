package org.limmen.mystart.exporter;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
public class Exporters {

  private final List<Exporter> exporters = new ArrayList<>();

  public Exporters() {
    log.info("Searching for bookmark exporters...");

    ServiceLoader<Exporter> serviceLoader = ServiceLoader.load(Exporter.class);
    for (Exporter e : serviceLoader) {
      log.info("Found exporter: {}", e.getName());
      exporters.add(e);
    }
  }
  
  public List<String> getAvailableExporters() {
    return this.exporters.stream()
            .map(Exporter::getName)
            .collect(Collectors.toList());
  }
}
