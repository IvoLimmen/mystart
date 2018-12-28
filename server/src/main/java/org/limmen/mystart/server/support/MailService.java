package org.limmen.mystart.server.support;

public interface MailService {

  void sendPasswordReset(String email, String fullName);
}
