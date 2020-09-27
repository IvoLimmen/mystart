package org.limmen.mystart.importer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.limmen.mystart.Link;

@Slf4j
public class AutoDetectParser extends AbstractParser {

  private final List<Parser> parsers = new ArrayList<>();

  private Parser parser = null;

  public AutoDetectParser() {
    log.info("Searching for bookmark importers...");

    ServiceLoader<Parser> serviceLoader = ServiceLoader.load(Parser.class);
    for (Parser p : serviceLoader) {
      log.info("Found parser: {}", p.getName());
      parsers.add(p);
    }
  }

  @Override
  public String getName() {
    return parser != null ? parser.getName() : "Autodetect parser";
  }

  @Override
  public boolean canParse(ParseContext context) {
    return true;
  }

  @Override
  public Set<Link> parse(ParseContext context) throws IOException {

    if (context.hasData()) {
      log.info("Reading file {} for autodetect", context.getFileName());
    } else {
      log.info("Reading URL '{}' for autodetect", context.getUrl());
    }

    this.parser = null;
    this.parsers.forEach(p -> {
      try {
        if (p.canParse(context)) {
          parser = p;
        }
      } catch (IOException ex) {
        log.error("Failed to detect type while scanning in {}", p.getName());
      }
    });

    if (parser == null) {
      throw new IllegalStateException("File can not be detected, parsing failed.");
    }

    log.info("Using {}...", parser.getName());

    return parser.parse(context);
  }
}
