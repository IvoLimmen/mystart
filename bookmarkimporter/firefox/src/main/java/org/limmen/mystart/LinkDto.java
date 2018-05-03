package org.limmen.mystart;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

public class LinkDto {

   private Integer id;

   private Integer parent;

   private String title;

   private String description;

   private LocalDateTime dateAdded;

   private LocalDateTime dateVisited;

   private Collection<String> labels = new ArrayList<>();

   private String url;

   public LocalDateTime getDateVisited() {
      return dateVisited;
   }

   public void setDateVisited(LocalDateTime dateVisited) {
      this.dateVisited = dateVisited;
   }

   public LocalDateTime getDateAdded() {
      return dateAdded;
   }

   public void setDateAdded(LocalDateTime dateAdded) {
      this.dateAdded = dateAdded;
   }

   public Integer getParent() {
      return parent;
   }

   public void setParent(Integer parent) {
      this.parent = parent;
   }

   public boolean isValid() {
      return url != null && !url.startsWith("place:") && !url.startsWith("javascript:");
   }

   public Integer getId() {
      return id;
   }

   public void setId(Integer id) {
      this.id = id;
   }

   public String getTitle() {
      return title;
   }

   public void setTitle(String title) {
      this.title = title;
   }

   public String getDescription() {
      return description;
   }

   public void setDescription(String description) {
      this.description = description;
   }

   public Collection<String> getLabels() {
      return labels;
   }

   public void addLabels(Collection<String> labels) {
      this.labels.addAll(labels);
   }

   public void setLabels(Collection<String> labels) {
      this.labels = labels;
   }

   public String getUrl() {
      return url;
   }

   public void setUrl(String url) {
      this.url = url;
   }
}
