package org.limmen.mystart.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Slf4jLog;
import org.limmen.mystart.importer.AutoDetectParser;
import org.limmen.mystart.importer.Parser;
import org.limmen.mystart.Storage;
import org.limmen.mystart.StorageProvider;
import org.limmen.mystart.server.support.MailService;
import org.limmen.mystart.server.support.MailServiceImpl;
import spark.Request;
import spark.Response;
import static spark.Spark.before;
import static spark.Spark.delete;
import static spark.Spark.exception;
import static spark.Spark.get;
import static spark.Spark.initExceptionHandler;
import static spark.Spark.path;
import static spark.Spark.port;
import static spark.Spark.post;
import static spark.Spark.put;
import static spark.Spark.staticFiles;

@Slf4j
public class Main {

  private static String configDir;  
  private static Environment environment;

  private static void parseArguments(String[] args) {
    for (int i = 0; i < args.length; i++) {
      switch (args[i]) {
        case "config.dir":
          configDir = args[++i];
          break;
        case "env":
          environment = Environment.valueOf(args[++i].toUpperCase());
          break;
        default:
          throw new IllegalArgumentException("Unknown program argument: " + args[i]);
      }
    }

    if (configDir == null || environment == null) {
      throw new IllegalArgumentException("Settings missing!");
    }

  }

  private static Path getFileName(String fileName) {
    int dot = fileName.indexOf(".");
    String baseName = fileName.substring(0, dot);
    String ext = fileName.substring(dot + 1);
    return Paths.get(configDir, String.format("%s-%s.%s", baseName, environment.name().toLowerCase(), ext));
  }
  
  public static void main(String[] args) throws Exception {
    parseArguments(args);
    
    Log.setLog(new Slf4jLog());
    Properties properties = new Properties();

    try (InputStream inputStream = Files.newInputStream(getFileName("application.properties"), StandardOpenOption.READ)) {
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

    UserHandler userHandler = new UserHandler(properties.getProperty("server.salt"), storage, mapper);
    LinkHandler linkHandler = new LinkHandler(storage, mapper);
    LabelHandler labelHandler = new LabelHandler(storage, mapper);

    initExceptionHandler((e) -> {
      log.error("Error during initialization:", e);
    });

    port(8080);

    staticFiles.location("/public");
    staticFiles.expireTime(3600); // hour

    exception(Exception.class, (exception, request, response) -> {
      log.error("Error", exception);
    });

    before("/api/*", (req, res) -> {
      // check for session/header key
      //req.cookie("mystart")
    });

    path("/api", () -> {
      path("/user", () -> {
        post("/login", userHandler::login);
        post("/logout", userHandler::login);
      });
      path("/link", () -> {
        get("/search", linkHandler::search);
        get("/by_label", linkHandler::byLabel);
        delete("/delete", linkHandler::delete);
        put("/visit", linkHandler::visit);
        put("/update", linkHandler::update);
      });
      path("/label", () -> {
        get("/all", labelHandler::all);
      });
    });
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
