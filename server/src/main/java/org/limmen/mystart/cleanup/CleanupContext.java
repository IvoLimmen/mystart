package org.limmen.mystart.cleanup;

public class CleanupContext {

   private long userId;

   private boolean markAsPrivateNetworkOnDomainError;

   private String url;

   public CleanupContext() {
   }

   public void setUrl(String url) {
      this.url = url;
   }

   public void setMarkAsPrivateNetworkOnDomainError(boolean markAsPrivateNetworkOnDomainError) {
      this.markAsPrivateNetworkOnDomainError = markAsPrivateNetworkOnDomainError;
   }

   public void setUserId(long userId) {
      this.userId = userId;
   }

   public long getUserId() {
      return userId;
   }

   public boolean markAsPrivateNetworkOnDomainError() {
      return markAsPrivateNetworkOnDomainError;
   }

   public String getUrl() {
      return url;
   }
}
