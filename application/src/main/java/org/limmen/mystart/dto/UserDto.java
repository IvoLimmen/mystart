package org.limmen.mystart.dto;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class UserDto {

  private String autoStartLabel;
  private String avatarFileName;
  private String email;
  private String fullName;
  private final Set<String> menuLabels = new HashSet<>();
  private boolean openInNewTab;
  private String password;
  private String password2;
  private String resetCode;
  private LocalDateTime resetCodeValid;

  public String getAutoStartLabel() {
    return autoStartLabel;
  }

  public void setAutoStartLabel(String autoStartLabel) {
    this.autoStartLabel = autoStartLabel;
  }

  public String getAvatarFileName() {
    return avatarFileName;
  }

  public void setAvatarFileName(String avatarFileName) {
    this.avatarFileName = avatarFileName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFullName() {
    return fullName;
  }

  public void setFullName(String fullName) {
    this.fullName = fullName;
  }

  public Set<String> getMenuLabels() {
    return menuLabels;
  }

  public boolean isOpenInNewTab() {
    return openInNewTab;
  }

  public void setOpenInNewTab(boolean openInNewTab) {
    this.openInNewTab = openInNewTab;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getResetCode() {
    return resetCode;
  }

  public void setResetCode(String resetCode) {
    this.resetCode = resetCode;
  }

  public LocalDateTime getResetCodeValid() {
    return resetCodeValid;
  }

  public void setResetCodeValid(LocalDateTime resetCodeValid) {
    this.resetCodeValid = resetCodeValid;
  }

  public void setPassword2(String password2) {
    this.password2 = password2;
  }

  public String getPassword2() {
    return password2;
  }
}
