package org.limmen.mystart;

import java.util.Properties;

public interface Storage {

  LinkStorage getLinkStorage();

  String getName();

  UserStorage getUserStorage();

  VisitStorage getVisitStorage();

  void initialize(Properties properties);
}
