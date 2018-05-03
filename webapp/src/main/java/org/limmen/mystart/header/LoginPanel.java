package org.limmen.mystart.header;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.limmen.mystart.page.IndexPage;
import org.limmen.mystart.model.LoginModel;

public class LoginPanel extends Panel {

  private static final long serialVersionUID = -7407129354080141202L;

  public LoginPanel(String id) {
    super(id);

    add(new LoginForm("loginForm"));
  }

  private class LoginForm extends Form<LoginModel> {

    private static final long serialVersionUID = -7490086894750771916L;

    public LoginForm(String id) {
      super(id, new CompoundPropertyModel<>(new LoginModel()));

      add(new EmailTextField("email"));
      add(new PasswordTextField("password"));
      add(new Button("submit") {
        private static final long serialVersionUID = 9123164874596936371L;

        @Override
        public void onSubmit() {
          super.onSubmit();
        }
      });
    }

    @Override
    protected void onSubmit() {
      super.onSubmit();

      LoginModel model = (LoginModel) getDefaultModel().getObject();
      AuthenticatedWebSession.get().signIn(model.getEmail(), model.getPassword());
      if (AuthenticatedWebSession.get().isSignedIn()) {
        setResponsePage(IndexPage.class);
      }
    }
  }
}
