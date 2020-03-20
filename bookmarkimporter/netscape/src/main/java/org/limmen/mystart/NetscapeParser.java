package org.limmen.mystart;

import java.io.IOException;
import java.util.Set;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class NetscapeParser extends AbstractParser {

  public NetscapeParser() {
    setSource("Netscape Bookmark");
  }

  @Override
  public String getName() {
    return "Netscape";
  }

  @Override
  public boolean canParse(ParseContext context) throws IOException {
    String data = getDataFromFile(context);
    return data != null && data.contains("NETSCAPE-Bookmark-file-1");
  }

  @Override
  public Set<Link> parse(ParseContext context) throws IOException {

    Document doc = Jsoup.parse(context.getInputStream(), "UTF-8", "http://example.com/");
    Elements elements = doc.select("H3");

    elements.forEach(e -> {
      String label = e.text();

      if (e.siblingElements().size() > 0) {
        Elements hrefs = e.siblingElements().first().select("A");
        if (hrefs.size() > 0) {
          hrefs.forEach(a -> {
            addLink(a.text(), a.attr("HREF"), label, a.text(), convertEpochToTimestamp(Long.parseLong(a.attr("ADD_DATE"))));
          });
        }
      }
    });

    return getLinks();
  }
}
