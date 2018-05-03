package org.limmen.mystart.header;

import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.limmen.mystart.model.SearchModel;

public class HeaderPanel extends Panel {

  private static final long serialVersionUID = -7407129354080141202L;

  public HeaderPanel(String id, boolean enabled) {
    super(id);

    add(new SearchInputForm("searchForm").setEnabled(enabled));
    add(new org.apache.wicket.markup.html.link.Link<Void>("logout") {
      
      private static final long serialVersionUID = -4331619903296515985L;

      @Override
      public void onClick() {
        AuthenticatedWebSession.get().invalidate();
        setResponsePage(getApplication().getHomePage());
      }
    }.setEnabled(enabled));    
  }

  private class SearchInputForm extends Form<SearchModel> {

    private static final long serialVersionUID = -7490086894750771916L;

    public SearchInputForm(String id) {
      super(id, new CompoundPropertyModel<>(new SearchModel()));

      add(new TextField<>("search", String.class));
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

      System.out.println(((SearchModel) getDefaultModel().getObject()).getSearch());
    }
  }
}
