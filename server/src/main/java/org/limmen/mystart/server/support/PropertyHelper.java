package org.limmen.mystart.server.support;

import java.util.Properties;

public class PropertyHelper {

  public static String getSalt(Properties properties) {
    return properties.getProperty("server.salt");
  }

  public static String getServerName(Properties properties) {
    return properties.getProperty("server.name");
  }

  private PropertyHelper() {
  }
}
