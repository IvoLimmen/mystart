package org.limmen.mystart.cleanup;

import java.time.LocalDateTime;
import org.limmen.mystart.Link;

public class CleanupResult {

  private final CleanupResultType cleanupResultType;

  private final Link link;

  private final String reason;

  public CleanupResult(Link link, String reason, CleanupResultType cleanupResultType) {
    this.link = link;
    this.reason = reason;
    this.cleanupResultType = cleanupResultType;
    link.setCheckResult(reason);
    link.setLastCheck(LocalDateTime.now());
  }

  public CleanupResultType getCleanupResultType() {
    return cleanupResultType;
  }

  public Link getLink() {
    return link;
  }

  public String getReason() {
    return reason;
  }

}
