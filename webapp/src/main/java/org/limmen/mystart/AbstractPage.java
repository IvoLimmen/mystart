package org.limmen.mystart;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.WebPage;
import org.limmen.mystart.exception.StorageException;
import org.limmen.mystart.header.HeaderPanel;

public abstract class AbstractPage extends WebPage {

  private static final long serialVersionUID = -3204554862615400939L;

  public AbstractPage() throws StorageException {    
    add(new HeaderPanel("header", AuthenticatedWebSession.get().isSignedIn()));    
  }
}
