package org.limmen.mystart.server.servlet;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.limmen.mystart.Link;
import org.limmen.mystart.Storage;
import org.limmen.mystart.importer.ParseContext;
import org.limmen.mystart.importer.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

public class ImportServlet extends AbstractServlet {

  private final static Logger log = LoggerFactory.getLogger(ImportServlet.class);
  
  private static final long serialVersionUID = 1L;

  private final Parser parser;

  public ImportServlet(Parser parser,
                       Storage storage,
                       Path temporaryDirectory, 
                       Properties properties) {
    super(storage, temporaryDirectory, properties);
    this.parser = parser;
  }

  private ParseContext handleFileUpload(Part filePart) throws IOException {
    String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
    Path tempFile = Paths.get(getTemporaryDirectory().toString(), UUID.randomUUID().toString());

    try (FileOutputStream fileOutputStream = new FileOutputStream(tempFile.toFile())) {
      fileOutputStream.write(IOUtils.toByteArray(filePart.getInputStream()));
    }

    return new ParseContext(new ByteArrayInputStream(IOUtils.toByteArray(filePart.getInputStream())), tempFile.toString(), fileName);
  }

  private ParseContext handleUrl(String url) {
    return new ParseContext(url);
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    super.doPost(req, res);

    Long userId = (Long) req.getSession().getAttribute(USER_ID);

    if (userId == null) {
      return;
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

    boolean skipDuplicates = getBool(req, "skipDuplicates");
    boolean importHomepageAsExtra = getBool(req, "importHomepageAsExtra");
    boolean importLanguageAsLabel = getBool(req, "importLanguageAsLabel");

    if (parseContext != null) {
      if (importHomepageAsExtra) {
        parseContext.addOption("github.importHomepageAsExtra", "true");
      }
      if (importLanguageAsLabel) {
        parseContext.addOption("github.importLanguageAsLabel", "true");
      }
    }

    Set<Link> links = parser.parse(parseContext);
    log.info("Parsed {} links using {}", links.size(), parser.getName());

    getLinkStorage().importCollection(userId, links, skipDuplicates);

    res.sendRedirect("/home");
  }
}
