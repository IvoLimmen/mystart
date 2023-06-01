package org.limmen.mystart.server.support;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MailServiceImpl implements MailService {

  private final static Logger log = LoggerFactory.getLogger(MailService.class);

  private static final String LINE_END = "\n";

  private final String from;

  private final String host;

  private final String password;

  private final int port;

  private Session session;

  private final boolean startTls;

  private final String username;

  public static Builder builder() {
    return new Builder();
  }
  
  public static class Builder {

    private String from;
    private String host;
    private String password;
    private int port;
    private boolean startTls;
    private String username;

    public Builder() {
    }

    public Builder from(String from) {
      this.from = from;
      return Builder.this;
    }

    public Builder host(String host) {
      this.host = host;
      return Builder.this;
    }

    public Builder password(String password) {
      this.password = password;
      return Builder.this;
    }

    public Builder port(int port) {
      this.port = port;
      return Builder.this;
    }

    public Builder startTls(boolean startTls) {
      this.startTls = startTls;
      return Builder.this;
    }

    public Builder username(String username) {
      this.username = username;
      return Builder.this;
    }

    public MailServiceImpl build() {
      return new MailServiceImpl(this);
    }
  }

  private MailServiceImpl(Builder builder) {
    this.from = builder.from;
    this.host = builder.host;
    this.startTls = builder.startTls;
    this.port = builder.port;
    this.password = builder.password;
    this.username = builder.username;
    createSession();
  }

  @Override
  public void sendPasswordReset(String email, String fullName, String link) {
    StringBuilder txt = new StringBuilder(512);
    txt.append("Hello ").append(fullName).append(", ").append(LINE_END);
    txt.append(LINE_END);
    txt.append("CLick here to reset your password: ").append(link).append(LINE_END);

    sendMessage("Your password reset link", String.format("%s <%s>", fullName, email), txt.toString());
  }

  private MimeMessage createMessage(String subject, String to) {
    MimeMessage message = new MimeMessage(session);
    try {
      message.setFrom(new InternetAddress(from));
      message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
      message.setSubject(subject);
    } catch (MessagingException e) {
      log.error("Failed to create MimeMessage.", e);
    }
    return message;
  }

  private void createSession() {
    Properties properties = new Properties();
    properties.putAll(System.getProperties());
    properties.setProperty("mail.smtp.host", host);
    properties.setProperty("mail.smtp.auth", "true");
    properties.setProperty("mail.transport.protocol", "smtp");
    properties.setProperty("mail.smtp.starttls.enable", startTls ? "true" : "false");
    properties.setProperty("mail.smtp.port", "" + port);
    properties.setProperty("mail.smtp.socketFactory.port", "" + port);
    properties.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

    this.session = Session.getDefaultInstance(properties, new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication(username, password);
      }
    });
  }

  private void sendMessage(String subject, String to, String msg) {
    try {
      MimeMessage message = this.createMessage(subject, to);
      message.setText(msg);

      Transport.send(message);

    } catch (MessagingException ex) {
      log.error("Failed to create and send message.", ex);
    }
  }

}
