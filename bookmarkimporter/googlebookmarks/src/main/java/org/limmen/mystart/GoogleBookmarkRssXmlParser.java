package org.limmen.mystart;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GoogleBookmarkRssXmlParser extends AbstractParser {

  private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.RFC_1123_DATE_TIME;

  public GoogleBookmarkRssXmlParser() {
    setSource("Google Bookmark");
  }

  @Override
  public String getName() {
    return "Google Bookmarks RSS XML";
  }

  @Override
  public boolean canParse(ParseContext context) throws IOException {
    String data = getDataFromFile(context);
    return data != null && data.contains("<rss version=\"2.0\"");
  }

  @Override
  public Set<Link> parse(ParseContext context) throws IOException {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(context.getInputStream());

      NodeList nodeList = doc.getElementsByTagName("item");

      for (int i = 0; i < nodeList.getLength(); i++) {
        parseBookmark(nodeList.item(i));
      }
    } catch (ParserConfigurationException | SAXException ex) {
      // ignore
    }

    return getLinks();
  }

  private void parseBookmark(Node item) {
    String title = null;
    String description = null;
    String url = null;
    LocalDateTime timestamp = null;
    List<String> labels = new ArrayList<>();

    NodeList children = item.getChildNodes();

    for (int i = 0; i < children.getLength(); i++) {
      Node node = children.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        switch (node.getNodeName()) {
          case "title":
            title = node.getTextContent();
            break;
          case "link":
            url = node.getTextContent();
            break;
          case "smh:bkmk_annotation":
            description = node.getTextContent();
            break;
          case "pubDate":
            timestamp = getTimestamp(node);
            break;
          case "smh:bkmk_label":
            labels.add(node.getTextContent());
            break;
        }
      }
    }

    addLink(title, url, labels, description, timestamp, null);
  }

  private LocalDateTime getTimestamp(Node node) {
    String time = node.getTextContent();
    return LocalDateTime.parse(time, DATE_FORMAT);
  }
}
