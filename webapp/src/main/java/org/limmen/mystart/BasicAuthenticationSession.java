package org.limmen.mystart;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.request.Request;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BasicAuthenticationSession extends AuthenticatedWebSession {

  private final static Logger LOGGER = LoggerFactory.getLogger(BasicAuthenticationSession.class);
  
  private static final long serialVersionUID = 7525364765643933965L;

  private String username;

  public BasicAuthenticationSession(Request request) {
    super(request);
  }

  @Override
  public boolean authenticate(String username, String password) {
    LOGGER.warn("authenticate: {}, {}", username, password);
    //user is authenticated if username and password are equal
    boolean authResult = username.equals(password);

    if (authResult) {
      this.username = username;
    }

    return true;
  }

  @Override
  public Roles getRoles() {
    return new Roles();
  }

  @Override
  public void signOut() {
    super.signOut();
    username = null;
  }
}
