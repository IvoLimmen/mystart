package org.limmen.mystart.page;

import org.limmen.mystart.AuthenticatedPage;
import org.limmen.mystart.content.ContentPanel;
import org.limmen.mystart.exception.StorageException;

public class IndexPage extends AuthenticatedPage {

  private static final long serialVersionUID = 1904489347149439623L;

  public IndexPage() throws StorageException {
    add(new ContentPanel("content"));
  }
}
