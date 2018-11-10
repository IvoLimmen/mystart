package org.limmen.mystart.server;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;
import javax.servlet.MultipartConfigElement;
import javax.servlet.SessionTrackingMode;
import javax.servlet.http.HttpServlet;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.jsp.JettyJspServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Slf4jLog;
import org.limmen.mystart.AutoDetectParser;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.Parser;
import org.limmen.mystart.Storage;
import org.limmen.mystart.StorageProvider;
import org.limmen.mystart.UserStorage;
import org.limmen.mystart.VisitStorage;
import org.limmen.mystart.server.servlet.HomeServlet;
import org.limmen.mystart.server.servlet.ImportServlet;
import org.limmen.mystart.server.servlet.LinkServlet;
import org.limmen.mystart.server.servlet.LoginServlet;
import org.limmen.mystart.server.servlet.UserServlet;
import org.limmen.mystart.server.servlet.ajax.AjaxServlet;

@Slf4j
public class Main {

  public static void main(String[] args) throws Exception {
    Log.setLog(new Slf4jLog());
    Config conf = ConfigFactory.load();

    Storage storage = StorageProvider.getStorageByName(conf, conf.getString("server.storage"));
    UserStorage userStorage = storage.getUserStorage();
    LinkStorage linkStorage = storage.getLinkStorage();
    VisitStorage visitStorage = storage.getVisitStorage();

    Parser parser = new AutoDetectParser();

    File scratchDir = createScratchDirectory();

    MultipartConfigElement multipartConfigElement = new MultipartConfigElement(scratchDir.getAbsolutePath(), 41943040, 41943040, 4096);

    Server server = new Server(conf.getInt("server.port"));
    URI baseUri = getWebRootResourceUri();
    Path avatarPath = Paths.get(new File(baseUri.toURL().toURI()).toPath().toString(), "avatar");
    Files.createDirectories(avatarPath);

    ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
    Set<SessionTrackingMode> trackingModes = new HashSet<>();
    trackingModes.add(SessionTrackingMode.COOKIE);
    servletContextHandler.getSessionHandler().setSessionTrackingModes(trackingModes);
    servletContextHandler.setContextPath("/");
    servletContextHandler.setResourceBase(baseUri.toASCIIString());
    ClassLoader jspClassLoader = new URLClassLoader(new URL[0], Main.class.getClassLoader());

    servletContextHandler.setAttribute("javax.servlet.context.tempdir", scratchDir);
    servletContextHandler.setClassLoader(jspClassLoader);

    servletContextHandler.addBean(new JspStarter(servletContextHandler));

    ServletHolder holderJsp = new ServletHolder("jsp", JettyJspServlet.class);
    holderJsp.setInitOrder(0);
    holderJsp.setInitParameter("logVerbosityLevel", "DEBUG");
    holderJsp.setInitParameter("fork", "false");
    holderJsp.setInitParameter("xpoweredBy", "false");
    holderJsp.setInitParameter("keepgenerated", "true");
    servletContextHandler.addServlet(holderJsp, "*.jsp");

    ServletHolder holderDefault = new ServletHolder("default", DefaultServlet.class);
    holderDefault.setInitParameter("resourceBase", baseUri.toASCIIString());
    holderDefault.setInitParameter("dirAllowed", "true");
    servletContextHandler.addServlet(holderDefault, "/");
    server.setHandler(servletContextHandler);

    addServlet(server, servletContextHandler, "homeServlet", "/home",
               new HomeServlet(linkStorage, userStorage, visitStorage, multipartConfigElement, scratchDir.toPath()));
    addServlet(server, servletContextHandler, "userServlet", "/user",
               new UserServlet(linkStorage, userStorage, visitStorage, multipartConfigElement, scratchDir.toPath(), avatarPath));
    addServlet(server, servletContextHandler, "loginServlet", "/login",
               new LoginServlet(linkStorage, userStorage, visitStorage, multipartConfigElement, scratchDir.toPath()));
    addServlet(server, servletContextHandler, "importServlet", "/import",
               new ImportServlet(parser, linkStorage, userStorage, visitStorage, multipartConfigElement, scratchDir.toPath()));
    addServlet(server, servletContextHandler, "linkServlet", "/link",
               new LinkServlet(linkStorage, userStorage, visitStorage, multipartConfigElement, scratchDir.toPath()));
    addServlet(server, servletContextHandler, "ajaxServlet", "/ajax",
               new AjaxServlet(linkStorage, userStorage, visitStorage, multipartConfigElement, scratchDir.toPath()));

    server.start();
    server.join();
  }

  private static void addServlet(Server server, ServletContextHandler servletContextHandler, String name, String url, HttpServlet servlet) {
    ServletHolder holderDefault = new ServletHolder(name, servlet);
    servletContextHandler.addServlet(holderDefault, url);
    server.setHandler(servletContextHandler);
  }

  private static File createScratchDirectory() throws IOException {
    File tempDir = new File(System.getProperty("java.io.tmpdir"));
    File scratchDir = new File(tempDir, "embedded-jetty-jsp");
    if (!scratchDir.exists()) {
      if (!scratchDir.mkdirs()) {
        throw new IOException("Unable to create scratch directory: " + scratchDir);
      }
    }
    return scratchDir;
  }

  private static URI getWebRootResourceUri() throws FileNotFoundException, URISyntaxException {
    URL indexUri = Main.class.getResource("../../../../webapp");
    if (indexUri == null) {
      throw new FileNotFoundException("Unable to find resources");
    }
    return indexUri.toURI();
  }
}
