package org.limmen.mystart.model;

import org.apache.wicket.util.io.IClusterable;

public class LoginModel implements IClusterable {

  private static final long serialVersionUID = 5459281929449208431L;

  private String email;

  private String password;

  public String getEmail() {
    return email;
  }

  public String getPassword() {
    return password;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setPassword(String password) {
    this.password = password;
  }

}
