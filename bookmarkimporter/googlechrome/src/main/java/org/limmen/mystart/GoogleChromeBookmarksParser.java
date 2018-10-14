package org.limmen.mystart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;

public class GoogleChromeBookmarksParser extends AbstractParser {

  public GoogleChromeBookmarksParser() {
    setSource("Chrome Bookmarks");
  }

  @Override
  public String getName() {
    return "Google Chrome";
  }

  @Override
  public boolean canParse(ParseContext context) throws IOException {
    String data = getDataFromFile(context);
    return data != null && data.contains("\"roots\": {");
  }

  @Override
  public Set<Link> parse(ParseContext context) throws IOException {

    StringBuilder json = new StringBuilder(4096);
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(context.getInputStream()))) {
      String line;
      while ((line = reader.readLine()) != null) {
        json.append(line);
      }
    }

    JSONObject jsonObject = new JSONObject(json.toString());
    JSONObject roots = jsonObject.getJSONObject("roots");
    JSONObject bbar = roots.getJSONObject("bookmark_bar");

    parseFolder(null, bbar);

    return getLinks();
  }

  private void parseFolder(String folder, JSONObject json) {
    JSONArray children = json.getJSONArray("children");

    for (int i = 0; i < children.length(); i++) {
      JSONObject child = children.getJSONObject(i);
      String name = child.getString("name");
      String type = child.getString("type");

      if ("folder".equals(type)) {
        if (folder != null) {
          name = folder + "," + name;
        }
        parseFolder(name, child);
      } else if ("url".equals(type)) {
        parseLink(folder, child);
      }
    }
  }

  private void parseLink(String folder, JSONObject json) {

    String url = json.getString("url");
    String title = json.getString("name");
    String time = json.getString("date_added");
    LocalDateTime timestamp = null;
    if (time != null) {
      long timeLong = (Long.parseLong(time) / 10);
      timestamp = convertEpochToTimestamp(timeLong);
    }

    List<String> labels = Arrays.asList(folder.split(","));

    addLink(title, url, labels, title, timestamp, null);
  }
}
