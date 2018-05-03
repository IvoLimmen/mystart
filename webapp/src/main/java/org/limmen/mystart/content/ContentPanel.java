package org.limmen.mystart.content;

import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.limmen.mystart.Link;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.exception.StorageException;

public class ContentPanel extends Panel {

  private static final long serialVersionUID = 4080307856575076281L;

  @Inject
  private LinkStorage linkStorage;
  
  public ContentPanel(String id) throws StorageException {
    super(id);

    List<Link> links = new ArrayList<>(this.linkStorage.getAll(1l));

    add(new ListView<Link>("linkList", links) {
      
      private static final long serialVersionUID = 4949588177564901031L;

      @Override
      protected void populateItem(ListItem<Link> item) {
        item.add(new LinkPanel("linkPanel", item.getModelObject(), item.size()));
      }
    });
  }
}
