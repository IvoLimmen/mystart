package org.limmen.mystart.content;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;
import org.limmen.mystart.Link;

public class LinkPanel extends Panel {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

  private static final long serialVersionUID = -7042029388581113412L;

  public LinkPanel(String id, Link link, int count) {
    super(id);

    String labels = link.getLabels().stream().collect(Collectors.joining(", "));
    String lastVisit = link.getLastVisit() == null ? "" : FORMATTER.format(link.getLastVisit());

    add(new ExternalLink("link", link));
    add(new Label("labels", labels));
    add(new Label("lastvisit", lastVisit));
  }

}
