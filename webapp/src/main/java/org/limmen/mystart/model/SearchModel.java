package org.limmen.mystart.model;

import org.apache.wicket.util.io.IClusterable;

public class SearchModel implements IClusterable {

   private static final long serialVersionUID = 5459281929449208431L;

   private String search;

   public String getSearch() {
      return search;
   }

   public void setSearch(String search) {
      this.search = search;
   }
}
