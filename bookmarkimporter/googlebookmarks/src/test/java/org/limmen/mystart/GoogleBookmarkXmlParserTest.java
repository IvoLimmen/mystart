package org.limmen.mystart;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
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
      List<Link> links = fixture.parse(new ParseContext(in, null, "/GoogleBookmarks.xml"));

      assertNotNull(links);
      assertTrue(links.size() == 2);

      assertEquals("Some title", links.get(0).getTitle());
      assertEquals("Some title", links.get(0).getDescription());
      assertEquals(1, links.get(0).getLabels().size());
      assertEquals("Label1", links.get(0).getLabels().iterator().next());

      assertEquals("Some title 2", links.get(1).getTitle());
      assertEquals("Some title 2", links.get(1).getDescription());
      assertEquals(2, links.get(1).getLabels().size());

      Iterator<String> labels = links.get(1).getLabels().iterator();

      assertEquals("Label1", labels.next());
      assertEquals("Label2", labels.next());
   }
}
