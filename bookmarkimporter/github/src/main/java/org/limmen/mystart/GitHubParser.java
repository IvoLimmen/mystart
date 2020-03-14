package org.limmen.mystart;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class GitHubParser extends AbstractParser implements Parser {

  private ParseContext context;

  public GitHubParser() {
    setSource("GitHub Stars");
  }

  @Override
  public boolean canParse(ParseContext context) {
    return context.getUrl() != null && context.getUrl().contains("github.com");
  }

  @Override
  public String getName() {
    return "GitHub";
  }

  @Override
  public Set<Link> parse(ParseContext context) throws IOException {
    this.context = context;

    GitHubPageLoader loader = new GitHubPageLoader();

    parseFile(loader.downloadFile(context.getUrl()));

    while (loader.hasMoreElements()) {
      parseFile(loader.nextElement());
    }

    return getLinks();
  }

  private LocalDateTime getTimestamp(JSONObject link) {
    String timeStr = link.getString("created_at");
    if (timeStr == null || timeStr.equals("")) {
      return null;
    }
    return LocalDateTime.parse(timeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'"));
  }

  private void parseFile(File file) throws IOException {
    StringBuilder json = new StringBuilder(4096);
    try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = reader.readLine()) != null) {
        json.append(line);
      }
    }

    JSONArray array = new JSONArray(json.toString());

    for (int i = 0; i < array.length(); i++) {
      JSONObject object = array.getJSONObject(i);
      parseProject(object);
    }
  }

  private void parseProject(JSONObject json) {
    String name = json.getString("name");
    String description = json.optString("description");

    List<String> labels = new ArrayList<>();
    if (json.has("language") && !json.isNull("language")) {
      if (context.hasOption("github.importLanguageAsLabel")) {
        labels.add(json.getString("language"));
      }
    }

    String href = json.getString("html_url");

    if (json.has("homepage") && !json.isNull("homepage")) {
      if (context.hasOption("github.importHomepageAsExtra")) {
        addLink(name, json.getString("homepage"), labels, description, getTimestamp(json), null);
      }
    }

    if (!StringUtils.isBlank(href)) {
      addLink(name, href, labels, description, getTimestamp(json), null);
    }
  }
}
