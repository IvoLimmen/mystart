package org.limmen.mystart.cleanup;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CleanupContext {

  private boolean assumeHttps;

  private boolean markAsPrivateNetworkOnDomainError;

  private int maximumTimeoutInSeconds;

  private long userId;
}
