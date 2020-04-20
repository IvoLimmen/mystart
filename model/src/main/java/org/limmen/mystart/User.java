package org.limmen.mystart;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class User extends BaseObject {

  private static final long serialVersionUID = -8464832961657168773L;

  private String autoStartLabel;
  private String avatarFileName;
  private String email;
  private String fullName;
  private final Set<String> menuLabels = new HashSet<>();
  private boolean openInNewTab;
  private String password;
  private String resetCode;
  private LocalDateTime resetCodeValid;

  public User() {
    this.openInNewTab = true;
    this.autoStartLabel = "MyStart";
  }

  public boolean check(String salt, String password) {
    if (this.password == null || salt == null || password == null) {
      return false;
    }
    return this.password.equals(encode(email, salt, password));
  }

  private String encode(String email, String salt, String password) {
    try {
      String text = email + salt + password;
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

  public String getAutoStartLabel() {
    return autoStartLabel;
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

  public Set<String> getMenuLabels() {
    return menuLabels;
  }

  public String getPassword() {
    return password;
  }

  public String getResetCode() {
    return resetCode;
  }

  public LocalDateTime getResetCodeValid() {
    return resetCodeValid;
  }

  public boolean isOpenInNewTab() {
    return openInNewTab;
  }

  public void setAutoStartLabel(String autoStartLabel) {
    this.autoStartLabel = autoStartLabel;
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

  public void setMenuLabels(Collection<String> menuLabels) {
    this.menuLabels.clear();
    this.menuLabels.addAll(menuLabels);
  }

  public void setOpenInNewTab(boolean openInNewTab) {
    this.openInNewTab = openInNewTab;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setResetCode(String resetCode) {
    this.resetCode = resetCode;
  }

  public void setResetCodeValid(LocalDateTime resetCodeValid) {
    this.resetCodeValid = resetCodeValid;
  }

  public void updatePassword(String salt, String password) {
    this.password = encode(email, salt, password);
  }
}
