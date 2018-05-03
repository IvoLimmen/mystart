package org.limmen.mystart;

import org.limmen.mystart.page.LoginPage;
import org.limmen.mystart.page.IndexPage;
import de.agilecoders.wicket.core.Bootstrap;
import javax.enterprise.inject.spi.CDI;
import org.apache.wicket.Page;
import org.apache.wicket.authroles.authentication.AbstractAuthenticatedWebSession;
import org.apache.wicket.authroles.authentication.AuthenticatedWebApplication;
import org.apache.wicket.cdi.CdiConfiguration;
import org.apache.wicket.markup.html.WebPage;

public class MyStartApplication extends AuthenticatedWebApplication {

  @Override
  public Class<? extends Page> getHomePage() {
    return IndexPage.class;
  }

  @Override
  protected Class<? extends WebPage> getSignInPageClass() {
    return LoginPage.class;
  }

  @Override
  protected Class<? extends AbstractAuthenticatedWebSession> getWebSessionClass() {
    return BasicAuthenticationSession.class;
  }

  @Override
  protected void init() {
    super.init();
    
    new CdiConfiguration(CDI.current().getBeanManager()).configure(this);
    
    mountPage("/index", IndexPage.class);
    mountPage("/login", LoginPage.class);
        
    Bootstrap.install(this);

    getSecuritySettings().setUnauthorizedComponentInstantiationListener(component -> {
      //component.setResponsePage(AuthWarningPage.class);
    });
  }
}
