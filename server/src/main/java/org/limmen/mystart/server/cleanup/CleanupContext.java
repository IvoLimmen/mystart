package org.limmen.mystart.server.cleanup;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CleanupContext {

  private boolean assumeHttps;

  private boolean markAsPrivateNetworkOnDomainError;

  private long userId;
}
