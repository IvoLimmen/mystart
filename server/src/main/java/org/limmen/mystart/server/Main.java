package org.limmen.mystart.server;

import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.FileAttribute;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.eclipse.jetty.http.HttpCookie;
import org.eclipse.jetty.jsp.JettyJspServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.limmen.mystart.Storage;
import org.limmen.mystart.StorageProvider;
import org.limmen.mystart.importer.AutoDetectParser;
import org.limmen.mystart.importer.Parser;
import org.limmen.mystart.server.servlet.HomeServlet;
import org.limmen.mystart.server.servlet.ImportServlet;
import org.limmen.mystart.server.servlet.LinkServlet;
import org.limmen.mystart.server.servlet.LoginServlet;
import org.limmen.mystart.server.servlet.NavServlet;
import org.limmen.mystart.server.servlet.UserServlet;
import org.limmen.mystart.server.servlet.ajax.AjaxServlet;
import org.limmen.mystart.server.support.MailService;
import org.limmen.mystart.server.support.MailServiceImpl;

import ch.qos.logback.classic.ClassicConstants;
import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletContext;
import jakarta.servlet.SessionTrackingMode;
import jakarta.servlet.http.HttpServlet;

public class Main {

  private static String configDir;
  private static String webappDir;
  private static Environment environment;

  private static void parseArguments(String[] args) {
    for (int i = 0; i < args.length; i++) {
      switch (args[i]) {
        case "config.dir":
          configDir = args[++i];
          break;
        case "webapp.dir":
          webappDir = args[++i];
          break;
        case "env":
          environment = Environment.valueOf(args[++i].toUpperCase());
          break;
        default:
          throw new IllegalArgumentException("Unknown program argument: " + args[i]);
      }
    }

    if (configDir == null || webappDir == null || environment == null) {
      throw new IllegalArgumentException("Settings missing!");
    }

  }

  private static String getFileName(String fileName) {
    int dot = fileName.indexOf(".");
    String baseName = fileName.substring(0, dot);
    String ext = fileName.substring(dot + 1);
    return String.format("%s-%s.%s", baseName, environment.name().toLowerCase(), ext);
  }

  public static void main(String[] args) throws Exception {
    parseArguments(args);

    System.setProperty(ClassicConstants.CONFIG_FILE_PROPERTY, Paths.get(configDir, getFileName("logback.xml")).toString());

    Properties properties = new Properties();
    try (InputStream inputStream = new FileInputStream(Paths.get(configDir, getFileName("application.properties")).toFile())) {
      properties.load(inputStream);
    }
    properties.putAll(System.getProperties());
    
    MailService mailService = MailServiceImpl.builder().from(properties.getProperty("mail.smtp.from"))
            .port(Integer.parseInt(properties.getProperty("mail.smtp.port"))).host(properties.getProperty("mail.smtp.host"))
            .startTls(Boolean.parseBoolean(properties.getProperty("mail.smtp.starttls")))
            .username(properties.getProperty("mail.smtp.username")).password(properties.getProperty("mail.smtp.password"))
            .build();

    String serverName = properties.getProperty("server.name");
    Integer port = Integer.parseInt(properties.getProperty("server.port", "8080"));
    String localUrl = createUrl(serverName);

    Storage storage = StorageProvider.getStorageByName(properties, properties.getProperty("server.storage"));

    Parser parser = new AutoDetectParser();
    // Exporters exporters = new Exporters();

    Path scratchDir = Files.createTempDirectory("embedded-jetty-jsp", new FileAttribute<?>[0]);

    MultipartConfigElement multipartConfigElement = new MultipartConfigElement(
            scratchDir.toString(),
            41943040,
            41943040,
            4096);

    Server server = new Server(port);
    URI baseUri = Paths.get(webappDir).toUri();
    Path avatarPath = Paths.get(webappDir, "avatar");
    Files.createDirectories(avatarPath);

    ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
    Set<SessionTrackingMode> trackingModes = new HashSet<>();
    trackingModes.add(SessionTrackingMode.COOKIE);
    servletContextHandler.getSessionHandler().setSessionTrackingModes(trackingModes);
    servletContextHandler.getSessionHandler().setHttpOnly(true);
    servletContextHandler.getSessionHandler().setSecureRequestOnly(true);
    servletContextHandler.getSessionHandler().setSameSite(HttpCookie.SameSite.STRICT);
    servletContextHandler.setContextPath("/");
    servletContextHandler.setResourceBase(baseUri.toASCIIString());
    ClassLoader jspClassLoader = new URLClassLoader(new URL[0], Main.class.getClassLoader());

    servletContextHandler.setAttribute(ServletContext.TEMPDIR, scratchDir.toFile());
    servletContextHandler.setClassLoader(jspClassLoader);

    servletContextHandler.addBean(new JspStarter(servletContextHandler));

    ServletHolder holderJsp = new ServletHolder("jsp", JettyJspServlet.class);
    holderJsp.setInitOrder(0);
    holderJsp.setInitParameter("logVerbosityLevel", "DEBUG");
    holderJsp.setInitParameter("fork", "false");
    holderJsp.setInitParameter("xpoweredBy", "false");
    holderJsp.setInitParameter("keepgenerated", "true");
    holderJsp.setInitParameter("scratchDir", scratchDir.toFile().toString());
    servletContextHandler.addServlet(holderJsp, "*.jsp");

    ServletHolder holderDefault = new ServletHolder("default", DefaultServlet.class);
    holderDefault.setInitParameter("resourceBase", baseUri.toASCIIString());
    holderDefault.setInitParameter("dirAllowed", "true");
    servletContextHandler.addServlet(holderDefault, "/");
    server.setHandler(servletContextHandler);

    addServlet(server, servletContextHandler, "homeServlet", "/home",
            new HomeServlet(storage, multipartConfigElement, scratchDir, properties));
    addServlet(server, servletContextHandler, "userServlet", "/user",
            new UserServlet(storage, multipartConfigElement, scratchDir, avatarPath, properties));
    addServlet(server, servletContextHandler, "loginServlet", "/login",
            new LoginServlet(storage, multipartConfigElement, scratchDir, mailService, localUrl, properties));
    addServlet(server, servletContextHandler, "importServlet", "/import",
            new ImportServlet(parser, storage, multipartConfigElement, scratchDir, properties));
    addServlet(server, servletContextHandler, "linkServlet", "/link",
            new LinkServlet(storage, multipartConfigElement, scratchDir, properties));
    addServlet(server, servletContextHandler, "navServlet", "/nav",
            new NavServlet(storage, multipartConfigElement, scratchDir, properties));
    addServlet(server, servletContextHandler, "ajaxServlet", "/ajax",
            new AjaxServlet(storage, multipartConfigElement, scratchDir, properties));

    server.start();
    server.join();
  }

  private static void addServlet(Server server,
                                 ServletContextHandler servletContextHandler,
                                 String name,
                                 String url,
                                 HttpServlet servlet) {
    ServletHolder holderDefault = new ServletHolder(name, servlet);
    servletContextHandler.addServlet(holderDefault, url);
    server.setHandler(servletContextHandler);
  }

  private static String createUrl(String serverName) {
    if (serverName.equals("localhost")) {
      return "http://" + serverName + ":8080";
    }

    return "https://" + serverName;
  }
}
