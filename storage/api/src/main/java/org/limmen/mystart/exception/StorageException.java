package org.limmen.mystart.exception;

public class StorageException extends Exception {

   private static final long serialVersionUID = -2697129203968210878L;

   public StorageException(String message) {
      super(message);
   }

   public StorageException(Throwable cause) {
      super(cause);
   }

   public StorageException(String message, Throwable cause) {
      super(message, cause);
   }
}
