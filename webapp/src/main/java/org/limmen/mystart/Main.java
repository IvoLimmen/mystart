package org.limmen.mystart;

import javax.enterprise.inject.se.SeContainer;
import javax.enterprise.inject.se.SeContainerInitializer;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class Main {

  public static void main(String[] args) throws Exception {
    SeContainerInitializer initializer = SeContainerInitializer.newInstance();
    try (SeContainer container = initializer.initialize()) {
      container.select(Main.class).get().start();
    }    
  }
  
  @Inject
  private WebServer webServer;
  
  public void start() throws Exception {
    webServer.start();
  }
}
