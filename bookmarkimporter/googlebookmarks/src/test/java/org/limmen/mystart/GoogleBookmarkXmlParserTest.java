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

public class GoogleBookmarkXmlParserTest {

  private final GoogleBookmarkXmlParser fixture = new GoogleBookmarkXmlParser();

  @Test
  public void parse() throws FileNotFoundException, IOException {
    InputStream inputStream = this.getClass().getResourceAsStream("/GoogleBookmarks.xml");
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    IOUtils.copy(inputStream, out);
    ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
    Set<Link> links = fixture.parse(new ParseContext(in, null, "/GoogleBookmarks.xml"));

    assertNotNull(links);
    assertTrue(links.size() == 2);

    Iterator<Link> it = links.iterator();
    Link link = it.next();
    assertEquals("Some title", link.getTitle());
    assertEquals("Some title", link.getDescription());
    assertEquals(1, link.getLabels().size());
    assertEquals("Label1", link.getLabels().iterator().next());

    link = it.next();
    assertEquals("Some title 2", link.getTitle());
    assertEquals("Some title 2", link.getDescription());
    assertEquals(2, link.getLabels().size());

    Iterator<String> labels = link.getLabels().iterator();

    assertEquals("Label1", labels.next());
    assertEquals("Label2", labels.next());
  }
}
