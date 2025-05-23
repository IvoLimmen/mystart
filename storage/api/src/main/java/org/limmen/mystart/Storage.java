package org.limmen.mystart;

import java.util.Properties;

public interface Storage {

  LinkStorage getLinkStorage();

  String getName();

  StatsStorage getStatsStorage();

  UserStorage getUserStorage();

  VisitStorage getVisitStorage();

  CategoryStorage getCategoryStorage();
  
  void initialize(Properties properties);
}
