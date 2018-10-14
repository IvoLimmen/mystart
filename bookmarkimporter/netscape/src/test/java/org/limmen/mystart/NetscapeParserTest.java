package org.limmen.mystart;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import org.apache.commons.io.IOUtils;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class NetscapeParserTest {

  private final NetscapeParser fixture = new NetscapeParser();

  @Test
  public void parse() throws FileNotFoundException, IOException {
    InputStream inputStream = this.getClass().getResourceAsStream("/GoogleBookmarks.html");
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    IOUtils.copy(inputStream, out);
    ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
    Set<Link> links = fixture.parse(new ParseContext(in, null, "/GoogleBookmarks.html"));

    assertNotNull(links);
    assertTrue(links.size() > 0);
  }
}
