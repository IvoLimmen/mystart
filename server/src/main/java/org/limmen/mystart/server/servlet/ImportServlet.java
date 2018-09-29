package org.limmen.mystart.server.servlet;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;
import org.limmen.mystart.Link;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.ParseContext;
import org.limmen.mystart.Parser;
import org.limmen.mystart.UserStorage;

@Slf4j
public class ImportServlet extends AbstractServlet {

  private static final long serialVersionUID = 1L;

  public ImportServlet(
      Parser parser,
      LinkStorage linkStorage,
      UserStorage userStorage,
      MultipartConfigElement multipartConfigElement,
      Path temporaryDirectory) {
    super(parser, linkStorage, userStorage, multipartConfigElement, temporaryDirectory);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    super.doPost(req, res);

    Part filePart = req.getPart("file");
    String url = req.getParameter("url");
    ParseContext parseContext = null;

    if (filePart != null) {
      parseContext = handleFileUpload(filePart);
    }
    if (url != null && url.length() > 0) {
      parseContext = handleUrl(url);
    }

    List<Link> links = getParser().parse(parseContext);
    log.info("Parsed {} links from {}", links.size(), getParser().getName());

    getLinkStorage().storeCollection(1L, links);

    res.sendRedirect("/home");
  }

  private ParseContext handleUrl(String url) {
    return new ParseContext(url);
  }

  private ParseContext handleFileUpload(Part filePart) throws IOException {
    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
    Path tempFile = Paths.get(getTemporaryDirectory().toString(), UUID.randomUUID().toString());

    try (FileOutputStream fileOutputStream = new FileOutputStream(tempFile.toFile())) {
      fileOutputStream.write(filePart.getInputStream().readAllBytes());
    }

    return new ParseContext(new ByteArrayInputStream(filePart.getInputStream().readAllBytes()), tempFile.toString(), fileName);
  }
}