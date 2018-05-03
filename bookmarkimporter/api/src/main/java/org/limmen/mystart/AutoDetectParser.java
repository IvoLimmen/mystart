package org.limmen.mystart;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

public class AutoDetectParser extends AbstractParser {

   private final List<Parser> parsers = new ArrayList<>();

   private Parser parser = null;

   public AutoDetectParser() {
      LOGGER.info("Searching for bookmark importers...");

      ServiceLoader<Parser> serviceLoader = ServiceLoader.load(Parser.class);
      for (Parser p : serviceLoader) {
         LOGGER.info("Found parser: {}", p.getName());
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
   public List<Link> parse(ParseContext context) throws IOException {

      if (context.hasData()) {
         LOGGER.info("Reading file {} for autodetect", context.getFileName());
      } else {
         LOGGER.info("Reading URL '{}' for autodetect", context.getUrl());         
      }

      this.parser = null;
      this.parsers.forEach(p -> {
         try {
            if (p.canParse(context)) {
               parser = p;
            }
         }
         catch (IOException ex) {
            LOGGER.error("Failed to detect type while scanning in {}", p.getName());
         }
      });

      if (parser == null) {
         throw new IllegalStateException("File can not be detected, parsing failed.");
      }

		LOGGER.info("Using {}...", parser.getName());         

      return parser.parse(context);
   }
}
