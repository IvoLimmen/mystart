package org.limmen.mystart.server;

import static spark.Spark.before;
import static spark.Spark.get;
import static spark.Spark.halt;
import static spark.Spark.path;
import static spark.Spark.port;
import static spark.Spark.staticFiles;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;

import org.limmen.mystart.AutoDetectParser;
import org.limmen.mystart.Parser;
import org.limmen.mystart.Storage;
import org.limmen.mystart.StorageProvider;
import org.limmen.mystart.User;
import org.limmen.mystart.server.support.MailService;
import org.limmen.mystart.server.support.MailServiceImpl;

import lombok.extern.slf4j.Slf4j;
import spark.Request;
import spark.Response;
import spark.Session;

@Slf4j
public class Main {

  public static final String USER = "user";
  public static final String USER_ID = "userId";

  public static void main(String[] args) throws IOException {

    Properties properties = new Properties();

    try (InputStream inputStream = Main.class.getResourceAsStream("/application.properties")) {
      properties.load(inputStream);
    }
    properties.putAll(System.getProperties());

    MailService mailService = MailServiceImpl.builder().from(properties.getProperty("mail.smtp.from"))
        .port(Integer.parseInt(properties.getProperty("mail.smtp.port"))).host(properties.getProperty("mail.smtp.host"))
        .startTls(Boolean.parseBoolean(properties.getProperty("mail.smtp.starttls")))
        .username(properties.getProperty("mail.smtp.username")).password(properties.getProperty("mail.smtp.password"))
        .build();

    Storage storage = StorageProvider.getStorageByName(properties, properties.getProperty("server.storage"));

    Parser parser = new AutoDetectParser();

    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(new Jdk8Module());
    JavaTimeModule module = new JavaTimeModule();
    module.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern("yyyyMMdd")));
    mapper.registerModule(module);

    LinkHandler linkHandler = new LinkHandler(storage, mapper);

    port(8080);

    staticFiles.location("/public");
    staticFiles.expireTime(3600); // hour

    before("/api/*", (req, res) -> {
      // check for session/header key
    });

    path("/api", () -> {
      path("/link", () -> {
        get("/search", (req, res) -> linkHandler.search(req, res));
      });
    });

    get("/command", (req, res) -> CommandHandler.handle(req, res));
  }

  protected static void addCookie(Response res, String key, String value) {
    int maxAge = 60 * 60 * 24 * 7; // week
    res.cookie("/", "mystart", value, maxAge, true, true);
  }

  protected static void clearCookies(Request req, Response res) {
    res.removeCookie("mystart");
    req.session().invalidate();
  }
}
