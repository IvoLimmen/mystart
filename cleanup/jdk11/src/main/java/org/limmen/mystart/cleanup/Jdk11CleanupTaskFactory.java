package org.limmen.mystart.cleanup;

import org.limmen.mystart.Link;

public class Jdk11CleanupTaskFactory implements CleanupTaskFactory {

  public Jdk11CleanupTaskFactory() {
  }

  @Override
  public CleanupTask newCleanupTask(Link link, CleanupContext context) {
    return new CleanupTaskJdk11(link, context);
  }
}
