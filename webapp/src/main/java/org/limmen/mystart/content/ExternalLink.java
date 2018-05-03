package org.limmen.mystart.content;

import java.time.LocalDateTime;
import javax.inject.Inject;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.pages.RedirectPage;
import org.apache.wicket.model.Model;
import org.limmen.mystart.Link;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.exception.StorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExternalLink extends org.apache.wicket.markup.html.link.Link<Link> {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExternalLink.class);

  private static final long serialVersionUID = -8695093379805029134L;

  private final Link link;
  
  @Inject
  private LinkStorage linkStorage;

  public ExternalLink(String id, Link link) {
    super(id);
    setDefaultModel(new Model<>(link.getUrl()));
    setBody(new Model<>(link.getTitle()));
    this.link = link;
  }

  @Override
  public void onClick() {
    try {
      link.setLastVisit(LocalDateTime.now());
      linkStorage.store(1l, link);
    } catch (StorageException ex) {
      LOGGER.error("Updating the visit failed", ex);
    }
    throw new RestartResponseAtInterceptPageException(new RedirectPage(getDefaultModelObjectAsString()));
  }

  @Override
  protected void onComponentTag(ComponentTag tag) {
    super.onComponentTag(tag);
    tag.put("target", "_blank");
  }
}
