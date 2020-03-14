package org.limmen.mystart;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class GoogleBookmarkXmlParser extends AbstractParser {

   public GoogleBookmarkXmlParser() {
      setSource("Google Bookmark");
   }

   @Override
   public String getName() {
      return "Google Bookmarks XML";
   }

   @Override
   public boolean canParse(ParseContext context) throws IOException {
      String data = getDataFromFile(context);
      return data != null && data.contains("<xml_api_reply version=\"1\">");
   }

   @Override
  public Set<Link> parse(ParseContext context) throws IOException {
      DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
      try {
         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
         Document doc = dBuilder.parse(context.getInputStream());

         NodeList nodeList = doc.getElementsByTagName("bookmark");

         for (int i = 0; i < nodeList.getLength(); i++) {
            parseBookmark(nodeList.item(i));
         }
      }
      catch (ParserConfigurationException | SAXException ex) {
         // ignore
      }

      return getLinks();
   }

   private void parseBookmark(Node item) {
      String title = null;
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
               case "url":
                  url = node.getTextContent();
                  break;
               case "timestamp":
                  timestamp = getTimestamp(node);
                  break;
               case "labels":
                  NodeList labelList = node.getChildNodes();
                  for (int j = 0; j < labelList.getLength(); j++) {
                     Node labelNode = labelList.item(j);
                     if (labelNode.getNodeType() == Node.ELEMENT_NODE && "label".equals(labelNode.getNodeName())) {
                        labels.add(labelNode.getTextContent());
                     }
                  }
                  break;
            }
         }
      }

      addLink(title, url, labels, title, timestamp, null);
   }

   private LocalDateTime getTimestamp(Node node) {
      long time = Long.parseLong(node.getTextContent());
      time = time / 1000;
      return LocalDateTime.ofInstant(new Date(time).toInstant(), ZoneId.systemDefault());
   }
}
