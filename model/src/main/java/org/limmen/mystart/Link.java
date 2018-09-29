package org.limmen.mystart;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Link extends BaseObject implements Comparable<Link> {

  private static final long serialVersionUID = -1267285018252976552L;

  private LocalDateTime creationDate;

  private String description;

  private String host;

  private final List<String> labels = new ArrayList<>();

  private LocalDateTime lastVisit;

  private boolean privateNetwork;

  private String source;

  private String title;

  private String url;

  public Link() {
    this.creationDate = LocalDateTime.now();
  }

  public Link(String url) {
    this();
    this.url = url;
    analyzeHost(url);
  }

  public void addLabel(String label) {
    this.labels.add(label);
  }

  @Override
  public int compareTo(Link o) {
    if (o == null) {
      return -1;
    }

    return this.url.compareTo(o.url);
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public String getDescription() {
    return description;
  }

  public String getHost() {
    return host;
  }

  public Collection<String> getLabels() {
    return labels;
  }

  public LocalDateTime getLastVisit() {
    return lastVisit;
  }
  public String getRedirectUrl() {
    if (url.startsWith("http:") || url.startsWith("https:")) {
      return getUrl();
    } else {
      return "https://" + getUrl();
    }
  }

  public String getSource() {
    return source;
  }

  public String getTitle() {
    return title;
  }

  public String getUrl() {
    return url;
  }

  /**
   * Will return <code>true</code> when the provided keyword is found, ignoring case, within the:
   * <ul>
   * <li>URL</li>
   * <li>Title</li>
   * <li>Description</li>
   * <li>Of of the labels</li>
   * </ul>
   *
   * @param keyword keyword to search for
   * @return <code>true</code> if found
   */
  public boolean hasKeyword(String keyword) {
    keyword = keyword.toLowerCase();
    return (getUrl() != null && getUrl().toLowerCase().contains(keyword))
        || (getTitle() != null && getTitle().toLowerCase().contains(keyword))
        || (getDescription() != null && getDescription().toLowerCase().contains(keyword))
        || hasKeywordInLabel(keyword);
  }

  /**
   * Will return <code>true</code> when the provided keyword is found, ignoring case, the labels
   *
   * @param keyword keyword to search for
   * @return <code>true</code> if found
   */
  public boolean hasKeywordInLabel(String keyword) {
    return this.labels.stream().map(l -> l.toLowerCase()).filter(f -> f.contains(keyword)).count() > 0;
  }

  public boolean isPrivateNetwork() {
    return privateNetwork;
  }

  public void setCreationDate(LocalDateTime creationDate) {
    this.creationDate = creationDate;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public void setLabels(Collection<String> labels) {
    this.labels.clear();
    this.labels.addAll(labels);
  }

  public void setLastVisit(LocalDateTime lastVisit) {
    this.lastVisit = lastVisit;
  }

  public void setPrivateNetwork(boolean privateNetwork) {
    this.privateNetwork = privateNetwork;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public void setUrl(String url) {
    if (this.url == null && url != null) {
      analyzeHost(url);
    }
    this.url = url;
  }

  @Override
  public String toString() {
    return "Link{" + "description=" + description + ", title=" + title + ", url=" + url + ", creationDate="
        + creationDate + ", lastVisit=" + lastVisit + ", labels=" + labels + ", source=" + source
        + ", privateNetwork=" + privateNetwork + '}';
  }

  public void visited() {
    this.lastVisit = LocalDateTime.now();
  }

  private void analyzeHost(String url) {
    try {
      this.host = new URI(url).getHost();
    } catch (URISyntaxException ex) {
      // ignore
    }
  }
}
