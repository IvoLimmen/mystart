package org.limmen.mystart.importer;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinkDto {

   private Integer id;

   private Integer parent;

   private String title;

   private String description;

   private LocalDateTime dateAdded;

   private LocalDateTime dateVisited;

   private Collection<String> labels = new ArrayList<>();

   private String url;

   public boolean isValid() {
      return url != null && !url.startsWith("place:") && !url.startsWith("javascript:");
   }

   public void addLabels(Collection<String> labels) {
      this.labels.addAll(labels);
   }
}
