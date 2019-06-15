package org.limmen.mystart;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XbelParser extends AbstractParser {

  public XbelParser() {
    setSource("XBEL");
  }

  @Override
  public String getName() {
    return "XBEL";
  }

  @Override
  public boolean canParse(ParseContext context) throws IOException {
    String data = getDataFromFile(context);
    return data != null && data.contains("xbel-1.0.dtd");
  }

  @Override
  public Set<Link> parse(ParseContext context) throws IOException {
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
      Document doc = dBuilder.parse(new File(context.getFileName()));

      Node root = doc.getDocumentElement();

      parseNodeList(root.getChildNodes(), new ArrayList<>());
    } catch (ParserConfigurationException | SAXException ex) {
      // ignore
    }

    return getLinks();
  }

  private void parseNodeList(NodeList nodeList, Collection<String> labels) {
    for (int i = 0; i < nodeList.getLength(); i++) {
      Node node = nodeList.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        if (node.getNodeName().equals("bookmark")) {
          parseBookmark(node, labels);
        } else if (node.getNodeName().equals("folder")) {
          parseFolder(node, labels);
        }
      }
    }
  }

  private void parseFolder(Node item, Collection<String> labels) {
    NodeList children = item.getChildNodes();

    for (int i = 0; i < children.getLength(); i++) {
      Node node = children.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        switch (node.getNodeName()) {
          case "title":
            labels.add(node.getTextContent());
            break;
          case "bookmark":
            parseBookmark(node, labels);
          case "folder":
            parseFolder(node, labels);
        }
      }
    }
  }

  private void parseBookmark(Node item, Collection<String> labels) {

    NamedNodeMap map = item.getAttributes();

    String url = getAttr(map, "href");
    String added = getAttr(map, "added");
    String visited = getAttr(map, "visited");
    String title = null;
    String desc = null;
    LocalDateTime timestamp = null;
    LocalDateTime timestampVisited = null;

    if (added != null) {
      timestamp = LocalDateTime.parse(added, DateTimeFormatter.ISO_DATE_TIME);
    }
    if (visited != null) {
      timestampVisited = LocalDateTime.parse(visited, DateTimeFormatter.ISO_DATE_TIME);
    }

    NodeList children = item.getChildNodes();

    for (int i = 0; i < children.getLength(); i++) {
      Node node = children.item(i);
      if (node.getNodeType() == Node.ELEMENT_NODE) {
        switch (node.getNodeName()) {
          case "title":
            title = node.getTextContent();
            break;
          case "desc":
            desc = node.getTextContent();
            break;
        }
      }
    }

    addLink(title, url, labels, desc, timestamp, timestampVisited);
  }

  private String getAttr(NamedNodeMap map, String attrName) {
    if (map != null && map.getNamedItem(attrName) != null) {
      return map.getNamedItem(attrName).getNodeValue();
    }
    return null;
  }
}
