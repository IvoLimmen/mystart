package org.limmen.mystart;

import org.apache.wicket.Application;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.limmen.mystart.exception.StorageException;

public abstract class AuthenticatedPage extends AbstractPage {

  private static final long serialVersionUID = 7925188402709777566L;

  public AuthenticatedPage() throws StorageException {
  }
  
  @Override
  protected void onConfigure() {
    super.onConfigure();
    AuthenticatedWebApplication app = (AuthenticatedWebApplication) Application.get();
    //if user is not signed in, redirect him to sign in page
    if (!AuthenticatedWebSession.get().isSignedIn()) {
      app.restartResponseAtSignInPage();
    }
  }
}
