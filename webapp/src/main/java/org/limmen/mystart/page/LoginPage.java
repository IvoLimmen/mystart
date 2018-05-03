package org.limmen.mystart.page;

import org.limmen.mystart.AbstractPage;
import org.limmen.mystart.exception.StorageException;
import org.limmen.mystart.header.LoginPanel;

public class LoginPage extends AbstractPage {

  private static final long serialVersionUID = 1904489347149439623L;

  public LoginPage() throws StorageException {    
    add(new LoginPanel("content"));
  }
}
