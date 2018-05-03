package org.limmen.mystart;

import java.util.EnumSet;
import javax.enterprise.context.ApplicationScoped;
import javax.servlet.DispatcherType;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.plus.webapp.PlusConfiguration;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.JettyWebXmlConfiguration;
import org.eclipse.jetty.webapp.MetaInfConfiguration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebXmlConfiguration;
import org.jboss.weld.environment.servlet.Listener;

@ApplicationScoped
public class WebServerImpl {

  public void start() throws Exception {
    System.setProperty("org.eclipse.jetty.util.log.class", "org.eclipse.jetty.util.log.Slf4jLog");
    Server server = new Server(8080);

    WebAppContext context = new WebAppContext();
    context.setConfigurations(new Configuration[]{
      new AnnotationConfiguration(),
      new WebXmlConfiguration(),
      new MetaInfConfiguration(),
      new FragmentConfiguration(),
      new PlusConfiguration(),
      new JettyWebXmlConfiguration()}
    );
    context.setContextPath("/");
    context.setResourceBase("src/main/webapp");
    context.setParentLoaderPriority(true);
    context.setClassLoader(Thread.currentThread().getContextClassLoader());
    context.addEventListener(new Listener());

    FilterHolder myStartFilterHolder = new FilterHolder(MyStartFilter.class);
    myStartFilterHolder.setInitParameter("applicationClassName", MyStartApplication.class.getCanonicalName());
    myStartFilterHolder.setInitParameter("filterMappingUrlPattern", "/*");
    myStartFilterHolder.setName("wicketFilter");

    context.addFilter(myStartFilterHolder, "/*", EnumSet.allOf(DispatcherType.class));

    server.setHandler(context);
    server.start();
    server.join();
  }
}
