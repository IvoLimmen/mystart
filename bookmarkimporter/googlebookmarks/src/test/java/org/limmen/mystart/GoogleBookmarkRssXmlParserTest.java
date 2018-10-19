package org.limmen.mystart;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class GoogleBookmarkRssXmlParserTest {

  private final GoogleBookmarkRssXmlParser fixture = new GoogleBookmarkRssXmlParser();

  @Test
  public void parse() throws FileNotFoundException, IOException {
    InputStream inputStream = this.getClass().getResourceAsStream("/GoogleBookmarks.rss");
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    IOUtils.copy(inputStream, out);
    ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
    Set<Link> links = fixture.parse(new ParseContext(in, null, "/GoogleBookmarks.rss"));

    assertNotNull(links);
    assertTrue(links.size() == 2);

    Iterator<Link> it = links.iterator();
    Link link = it.next();
    assertEquals("Some title", link.getTitle());
    assertEquals("Some description", link.getDescription());
    assertEquals(2, link.getLabels().size());
    Iterator<String> labelIt = link.getLabels().iterator();
    assertEquals("Label1", labelIt.next());
    assertEquals("Label2", labelIt.next());

    link = it.next();
    assertEquals("Another title", link.getTitle());
    assertEquals("Bla bla", link.getDescription());
    assertEquals(1, link.getLabels().size());

    Iterator<String> labels = link.getLabels().iterator();

    assertEquals("Label1", labels.next());
  }
}
