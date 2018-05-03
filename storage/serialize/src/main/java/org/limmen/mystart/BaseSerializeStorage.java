package org.limmen.mystart;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.limmen.mystart.exception.StorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseSerializeStorage {

   protected static final Logger LOGGER = LoggerFactory.getLogger(BaseSerializeStorage.class);

   protected String getFileName(String file) {
      File myStartConfigDir = new File(System.getProperty("user.home"), ".myStart");
      if (!myStartConfigDir.exists()) {
         myStartConfigDir.mkdir();
      }
      return new File(myStartConfigDir, file).getAbsolutePath();
   }
   
   @SuppressWarnings("unchecked")
   protected <T> T load(String file) throws StorageException {
      try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
         return (T) in.readObject();
      }
      catch (IOException | ClassNotFoundException e) {
         throw new StorageException(e);
      }
   }

   protected void save(Object object, String file) throws StorageException {
      try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
         out.writeObject(object);
      }
      catch (IOException ioe) {
         throw new StorageException(ioe);
      }
   }
}
