package org.limmen.mystart.support;

import java.net.URI;

public interface MailService {
  void sendPasswordReset(String email, String fullName, URI link);

  void sendValidateEmail(String email, String fullName, URI link);
}
