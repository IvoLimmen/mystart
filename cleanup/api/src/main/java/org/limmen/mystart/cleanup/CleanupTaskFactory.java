package org.limmen.mystart.cleanup;

import org.limmen.mystart.Link;

public interface CleanupTaskFactory {

  CleanupTask newCleanupTask(Link link, CleanupContext context);
}
