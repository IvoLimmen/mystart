package org.limmen.mystart.support;

import java.net.URI;

import org.limmen.mystart.config.SettingsProvider;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.api.mailer.MailerRegularBuilder;
import org.simplejavamail.api.mailer.config.TransportStrategy;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

import jakarta.inject.Singleton;
import jakarta.mail.Message.RecipientType;

@Singleton
public class MailServiceImpl implements MailService {

  private static final String LINE_END = "\n";
  private final Mailer mailer;
  private final String from;

  public MailServiceImpl(SettingsProvider settingsProvider) {

    MailerRegularBuilder<?> builder = MailerBuilder
        .withSMTPServer(settingsProvider.string("mail.smtp.host"), settingsProvider.integer("mail.smtp.port"))
        .withDebugLogging(settingsProvider.bool("mail.smtp.debug"))
        .withSMTPServerUsername(settingsProvider.string("mail.smtp.username"))
        .withSMTPServerPassword(settingsProvider.string("mail.smtp.password"));
    
    if (settingsProvider.bool("mail.smtp.starttls")) {
      builder = builder.withTransportStrategy(TransportStrategy.SMTP_TLS);
    } else {
      builder = builder.withTransportStrategy(TransportStrategy.SMTPS);
    }
    
    this.mailer = builder.buildMailer();
    this.from = settingsProvider.string("mail.smtp.from");
  }

  @Override
  public void sendPasswordReset(String emailAddress, String fullName, URI link) {

    StringBuilder txt = new StringBuilder(512);
    txt.append("Hello ").append(fullName).append(", ").append(LINE_END);
    txt.append(LINE_END);
    txt.append("CLick here to reset your password: ").append(link).append(LINE_END);

    this.mailer.sendMail(EmailBuilder
        .startingBlank()
        .withRecipient(fullName, emailAddress, RecipientType.TO)
        .from("MyStart Mailer", from)
        .withSubject("Reset your password")
        .appendText(txt.toString())
        .buildEmail());
  }

  @Override
  public void sendValidateEmail(String emailAddress, String fullName, URI link) {
    StringBuilder txt = new StringBuilder(512);
    txt.append("Hello ").append(fullName).append(", ").append(LINE_END);
    txt.append(LINE_END);
    txt.append("Click here to validate your email address: ").append(link).append(LINE_END);

    this.mailer.sendMail(EmailBuilder
        .startingBlank()
        .withRecipient(fullName, emailAddress, RecipientType.TO)
        .from("MyStart Mailer", from)
        .withSubject("Confirm your email address")
        .appendText(txt.toString())
        .buildEmail());
  }
}
