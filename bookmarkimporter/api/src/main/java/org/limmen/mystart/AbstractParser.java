package org.limmen.mystart;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractParser implements Parser {

   protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractParser.class);

   private final List<Link> links = new ArrayList<>();

   private String source;

   protected void addLink(String title, String href, String label, String description, LocalDateTime creationDateTime) {
      addLink(title, href, Collections.singletonList(label), description, creationDateTime, null);
   }

   protected void addLink(String title, String href, Collection<String> labels, String description,
       LocalDateTime creationDateTime, LocalDateTime lastVisitDate) {

      if (title == null) {
         title = "No title";
      }

      Link link = new Link(href);
      link.setTitle(title);
      link.setDescription(description);
      link.setLastVisit(lastVisitDate);
      link.setCreationDate(creationDateTime);
      link.setSource(source);
      link.setLabels(labels);

      this.links.add(link);
   }

   final public void setSource(String source) {
      this.source = source;
   }

   public List<Link> getLinks() {
      return links;
   }

   protected LocalDateTime convertEpochToTimestamp(final long dateTime) {
      long time = (dateTime / 1000);
      return LocalDateTime.ofInstant(new Date(time).toInstant(), ZoneId.systemDefault());
   }

   protected String getDataFromFile(ParseContext context) {
      if (context.hasData()) {
         try {
            StringWriter writer = new StringWriter();
            IOUtils.copy(new InputStreamReader(context.getInputStream()), writer);
            return writer.toString();
         }
         catch (IOException ex) {
            return null;
         }
      } else {
         return null;
      }
   }
}
