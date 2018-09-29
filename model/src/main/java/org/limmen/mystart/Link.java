package org.limmen.mystart;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Link extends BaseObject implements Comparable<Link> {

  private static final long serialVersionUID = -1267285018252976552L;

  private String description;

  private String title;

  private String url;

  private String host;

  private LocalDateTime creationDate;

  private LocalDateTime lastVisit;

  private final List<String> labels = new ArrayList<>();

  private String source;

  private boolean privateNetwork;

  public Link(String url) {
    this.url = url;
    this.creationDate = LocalDateTime.now();

    try {
      this.host = new URI(url).getHost();
    } catch (URISyntaxException ex) {
      // ignore
    }

    // generate an ID
    if (this.url != null) {
      this.id = Long.valueOf(this.url.hashCode());
    }
  }

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public LocalDateTime getLastVisit() {
    return lastVisit;
  }

  public void setLastVisit(LocalDateTime lastVisit) {
    this.lastVisit = lastVisit;
  }

  public boolean isPrivateNetwork() {
    return privateNetwork;
  }

  public void setPrivateNetwork(boolean privateNetwork) {
    this.privateNetwork = privateNetwork;
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

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public Collection<String> getLabels() {
    return labels;
  }

  public void setLabels(Collection<String> labels) {
    this.labels.clear();
    this.labels.addAll(labels);
  }

  public void addLabel(String label) {
    this.labels.add(label);
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDateTime creationDate) {
    this.creationDate = creationDate;
  }

  @Override
  public String toString() {
    return "Link{" + "description=" + description + ", title=" + title + ", url=" + url + ", creationDate="
        + creationDate + ", lastVisit=" + lastVisit + ", labels=" + labels + ", source=" + source
        + ", privateNetwork=" + privateNetwork + '}';
  }

  @Override
  public int compareTo(Link o) {
    if (o == null) {
      return -1;
    }

    return this.url.compareTo(o.url);
  }

  public void visited() {
    this.lastVisit = LocalDateTime.now();
  }
}
