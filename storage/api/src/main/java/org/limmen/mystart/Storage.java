package org.limmen.mystart;

import com.typesafe.config.Config;

public interface Storage {

  LinkStorage getLinkStorage();

  String getName();

  UserStorage getUserStorage();

  VisitStorage getVisitStorage();

  void initialize(Config conf);
}
