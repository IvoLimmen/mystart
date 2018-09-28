package org.limmen.mystart.server.servlet;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jetty.server.Request;
import org.limmen.mystart.Link;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.ParseContext;
import org.limmen.mystart.Parser;
import org.limmen.mystart.exception.StorageException;

@Slf4j
public class ImportServlet extends HttpServlet {

  private static final long serialVersionUID = 1L;

  private final Parser parser;

  private final LinkStorage linkStorage;

  private final MultipartConfigElement multipartConfigElement;

  public ImportServlet(Parser parser, LinkStorage linkStorage, MultipartConfigElement multipartConfigElement) {
    this.parser = parser;
    this.linkStorage = linkStorage;
    this.multipartConfigElement = multipartConfigElement;
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    if (req.getContentType() != null && req.getContentType().startsWith("multipart/form-data")) {
      req.setAttribute(Request.__MULTIPART_CONFIG_ELEMENT, multipartConfigElement);
    }

    Part filePart = req.getPart("file");
    String url = req.getParameter("url");
    ParseContext parseContext = null;

    if (filePart != null) {
      parseContext = handleFileUpload(filePart);
    }
    if (url != null && url.length() > 0) {
      parseContext = handleUrl(url);
    }

    List<Link> links = parser.parse(parseContext);
    log.info("Parsed {} links from {}", links.size(), parser.getName());

    try {
      linkStorage.storeCollection(1L, links);
    } catch (StorageException ex) {
      throw new ServletException(ex);
    }
  }

  private ParseContext handleUrl(String url) {
    return new ParseContext(url);
  }

  private ParseContext handleFileUpload(Part filePart) throws IOException {
    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
    return new ParseContext(new ByteArrayInputStream(filePart.getInputStream().readAllBytes()), fileName, fileName);
  }
}
