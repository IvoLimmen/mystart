package org.limmen.mystart;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class User extends BaseObject {

  private static final long serialVersionUID = -8464832961657168773L;

  private String avatarFileName;
  private String email;
  private String fullName;
  private String name;
  private boolean openInNewTab;
  private String password;

  public User(String name, String fullName, String email, String avatarFileName, String password) {
    this.email = email;
    this.name = name;
    this.fullName = fullName;
    this.avatarFileName = avatarFileName;
    this.password = encode(email, password);
  }

  public boolean check(String password) {
    return this.password.equals(encode(email, password));
  }

  public String getAvatarFileName() {
    return avatarFileName;
  }

  public String getEmail() {
    return email;
  }

  public String getFullName() {
    return fullName;
  }

  public String getName() {
    return name;
  }

  public String getPassword() {
    return password;
  }

  public boolean isOpenInNewTab() {
    return openInNewTab;
  }

  public void setAvatarFileName(String avatarFileName) {
    this.avatarFileName = avatarFileName;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setOpenInNewTab(boolean openInNewTab) {
    this.openInNewTab = openInNewTab;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void updatePassword(String password) {
    this.password = encode(email, password);
  }

  private String encode(String email, String password) {
    try {
      String text = email + "/myStartSalt/" + password;
      MessageDigest md = MessageDigest.getInstance("SHA-1");
      StringBuilder pw = new StringBuilder(64);
      for (byte b : md.digest(text.getBytes("UTF-8"))) {
        pw.append(String.format("%02x", b));
      }
      return pw.toString();
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }
}
