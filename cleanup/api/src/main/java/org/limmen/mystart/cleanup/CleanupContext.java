package org.limmen.mystart.cleanup;

public class CleanupContext {

  boolean assumeHttps;
  boolean markAsPrivateNetworkOnDomainError;
  int maximumTimeoutInSeconds;
  long userId;

  public boolean getAssumeHttps() {
    return assumeHttps;
  }

  public boolean getMarkAsPrivateNetworkOnDomainError() {
    return markAsPrivateNetworkOnDomainError;
  }

  public int getMaximumTimeoutInSeconds() {
    return maximumTimeoutInSeconds;
  }

  public long getUserId() {
    return userId;
  }

  public static Builder builder() {
    return new Builder();
  }

  public static class Builder {

    private boolean assumeHttps;
    private boolean markAsPrivateNetworkOnDomainError;
    private int maximumTimeoutInSeconds;
    private long userId;

    public Builder() {
    }

    public Builder assumeHttps(boolean assumeHttps) {
      this.assumeHttps = assumeHttps;
      return Builder.this;
    }

    public Builder markAsPrivateNetworkOnDomainError(boolean markAsPrivateNetworkOnDomainError) {
      this.markAsPrivateNetworkOnDomainError = markAsPrivateNetworkOnDomainError;
      return Builder.this;
    }

    public Builder maximumTimeoutInSeconds(int maximumTimeoutInSeconds) {
      this.maximumTimeoutInSeconds = maximumTimeoutInSeconds;
      return Builder.this;
    }

    public Builder userId(long userId) {
      this.userId = userId;
      return Builder.this;
    }

    public CleanupContext build() {

      return new CleanupContext(this);
    }
  }

  private CleanupContext(Builder builder) {
    this.assumeHttps = builder.assumeHttps;
    this.markAsPrivateNetworkOnDomainError = builder.markAsPrivateNetworkOnDomainError;
    this.maximumTimeoutInSeconds = builder.maximumTimeoutInSeconds;
    this.userId = builder.userId;
  }

  public boolean isAssumeHttps() {
    return assumeHttps;
  }

  public boolean isMarkAsPrivateNetworkOnDomainError() {
    return markAsPrivateNetworkOnDomainError;
  }  
}
