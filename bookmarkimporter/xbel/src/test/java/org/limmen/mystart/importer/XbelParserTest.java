package org.limmen.mystart.importer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.limmen.mystart.Link;

public class XbelParserTest {

  private final XbelParser fixture = new XbelParser();

  @Test
  public void parse() throws FileNotFoundException, IOException {
    File path = new File(System.getProperty("user.dir"), "src");
    path = new File(path, "test");
    path = new File(path, "resources");

    Set<Link> links = fixture.parse(new ParseContext(null, path.getAbsolutePath() + "/linksagogo.xbel", path.getAbsolutePath() + "/linksagogo.xbel"));

    assertNotNull(links);
    assertEquals(81, links.size());
  }

  @Test
  public void parse2() throws FileNotFoundException, IOException {
    File path = new File(System.getProperty("user.dir"), "src");
    path = new File(path, "test");
    path = new File(path, "resources");

    Set<Link> links = fixture.parse(new ParseContext(null, path.getAbsolutePath() + "/Linux_at_Home_Links.xbel", path.getAbsolutePath() + "/Linux_at_Home_Links.xbel"));

    assertNotNull(links);
    assertEquals(232, links.size());
  }
}
