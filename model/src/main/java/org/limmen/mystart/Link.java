package org.limmen.mystart;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Link extends BaseObject implements Comparable<Link> {

  private static final String UNCATEGORIZED = "Uncategorized";

  private static final long serialVersionUID = -1267285018252976552L;

  public static String sanatizeUrl(String url) {
    Objects.requireNonNull(url, "URL may nog be null!");

    url = url.toLowerCase();
    if (url.endsWith("/")) {
      url = url.substring(0, url.length() - 1);
    }
    if (!url.startsWith("http")) {
      url = "https://" + url;
    }

    return url;
  }

  private String checkResult;

  private LocalDateTime creationDate;

  private String description;

  private final Set<String> labels = new HashSet<>();

  private LocalDateTime lastCheck;

  private LocalDateTime lastVisit;

  private String source;

  private String title;

  private String url;

  public Link() {
    this.creationDate = LocalDateTime.now();
    this.source = "MyStart";
  }

  public Link(String url) {
    this();

    Objects.requireNonNull(url, "URL may nog be null!");

    this.url = sanatizeUrl(url);
  }

  public void addLabel(String label) {
    this.labels.add(label);
  }

  public void addLabels(Collection<String> labels) {
    this.labels.addAll(labels);
  }

  @Override
  public int compareTo(Link o) {
    if (o == null) {
      return -1;
    }

    return this.url.compareTo(o.url);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    final Link other = (Link) obj;
    if (!Objects.equals(this.url, other.url)) {
      return false;
    }
    return true;
  }

  public String getFormattedCreationDate() {
    return DateTimeFormatter
        .ofPattern("yyyy-MM-dd")
        .format(this.getCreationDate());
  }

  public String getFormattedLastCheckDate() {
    if (this.getLastCheck() == null) {
      return null;
    }
    return DateTimeFormatter
        .ofPattern("yyyy-MM-dd")
        .format(this.getLastCheck());
  }

  public String getFormattedLastVisit() {
    if (this.getLastVisit() == null) {
      return "Never";
    } else {
      Period visit = Period.between(getLastVisit().toLocalDate(), LocalDate.now());

      if (visit.getYears() > 1) {
        return visit.getYears() + " years ago";
      } else if (visit.getYears() == 1 && visit.getMonths() > 0) {
        return (visit.getMonths() + 12) + " months ago";
      } else if (visit.getYears() == 0 && visit.getMonths() > 0) {
        return visit.getMonths() + " months ago";
      } else if (visit.getYears() == 0 && visit.getMonths() == 1) {
        return "last month";
      } else if (visit.getYears() == 0 && visit.getMonths() == 0 && visit.getDays() > 14) {
        return "more than 2 weeks ago";
      } else if (visit.getYears() == 0 && visit.getMonths() == 0 && visit.getDays() > 7) {
        return "more than a week ago";
      } else if (visit.getYears() == 0 && visit.getMonths() == 0 && visit.getDays() > 2) {
        return "last week";
      } else if (visit.getYears() == 0 && visit.getMonths() == 0 && visit.getDays() > 0) {
        return "yesterday";
      } else {
        return "today";
      }
    }
  }

  public String getHost() {
    try {
      String host = new URI(this.url).getHost();
      if (host != null && host.startsWith("www")) {
        host = host.substring(4);
      }
      return host;
    } catch (URISyntaxException ex) {
      return null;
    }
  }

  public String getRedirectUrl() {
    if (url.startsWith("http:") || url.startsWith("https:")) {
      return getUrl();
    } else {
      return "https://" + getUrl();
    }
  }

  /**
   * Will return <code>true</code> when the provided keyword is found, ignoring
   * case, within the:
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
   * Will return <code>true</code> when the provided keyword is found, ignoring
   * case, the labels
   *
   * @param keyword keyword to search for
   * @return <code>true</code> if found
   */
  public boolean hasKeywordInLabel(String keyword) {
    return this.labels.stream().map(l -> l.toLowerCase()).filter(f -> f.contains(keyword)).count() > 0;
  }

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 23 * hash + Objects.hashCode(this.url);
    return hash;
  }

  public void moveLabel(String oldLabel, String newLabel) {
    this.addLabel(newLabel);
    this.removeLabel(oldLabel);
  }

  public void removeLabel(String label) {
    this.labels.remove(label);

    if (this.labels.isEmpty()) {
      this.labels.add(UNCATEGORIZED);
    }
  }

  public void setLabels(Collection<String> labels) {
    this.labels.clear();
    this.labels.addAll(labels);
  }

  public void visited() {
    this.lastVisit = LocalDateTime.now();
  }

  public String getCheckResult() {
    return checkResult;
  }

  public void setCheckResult(String checkResult) {
    this.checkResult = checkResult;
  }

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(LocalDateTime creationDate) {
    this.creationDate = creationDate;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Set<String> getLabels() {
    return labels;
  }

  public LocalDateTime getLastCheck() {
    return lastCheck;
  }

  public void setLastCheck(LocalDateTime lastCheck) {
    this.lastCheck = lastCheck;
  }

  public LocalDateTime getLastVisit() {
    return lastVisit;
  }

  public void setLastVisit(LocalDateTime lastVisit) {
    this.lastVisit = lastVisit;
  }

  public String getSource() {
    return source;
  }

  public void setSource(String source) {
    this.source = source;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }
}
