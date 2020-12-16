package org.limmen.mystart.importer;

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
import org.limmen.mystart.Link;

public class GitLabParser extends AbstractParser {

  public GitLabParser() {
    setSource("GitLab Starred");
  }

  @Override
  public boolean canParse(ParseContext context) {
    return context.getUrl() != null && context.getUrl().contains("gitlab.com");
  }

  @Override
  public String getName() {
    return "GitLab";
  }

  @Override
  public Set<Link> parse(ParseContext context) throws IOException {
    GitLabPageLoader loader = new GitLabPageLoader();

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
    return LocalDateTime.parse(timeStr, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"));
  }

  private void parseFile(File file) throws IOException {
    if (file == null) {
      return;
    }
    
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
    labels.add("GitLab");
    String href;
    if (json.has("web_url")) {
      href = json.getString("web_url");  
    } else {
      href = "https://gitlab.com/" + json.getString("path_with_namespace");
    }

    if (!StringUtils.isBlank(href)) {
      addLink(name, href, labels, description, getTimestamp(json), null);
    }
  }
}
