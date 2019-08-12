package org.limmen.mystart;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class FireFoxParserTest {

   private final Parser fixture = new FireFoxParser();

   @Test
   public void parse() throws FileNotFoundException, IOException {
      
      File path = new File(System.getProperty("user.dir"), "src");
      path = new File(path, "test");
      path = new File(path, "resources");
      
     Set<Link> links = fixture.parse(new ParseContext(null, path.getAbsolutePath() + "/places.sqlite", "/places.sqlite"));

      assertNotNull(links);
      assertTrue(links.size() == 1);

     Link link = links.iterator().next();

      assertEquals("http://example.com", link.getUrl());
      assertEquals("Example Domain", link.getTitle());
      assertEquals("Some Description On This Url", link.getDescription());
   }
}
