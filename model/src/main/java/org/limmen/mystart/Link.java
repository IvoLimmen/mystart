package org.limmen.mystart;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Link extends BaseObject implements Comparable<Link> {

  private static final long serialVersionUID = -1267285018252976552L;

  private LocalDateTime creationDate;

  private String description;

  private final Set<String> labels = new HashSet<>();

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

  public LocalDateTime getCreationDate() {
    return creationDate;
  }

  public String getDescription() {
    return description;
  }

  public String getFormattedCreationDate() {
    return DateTimeFormatter
        .ofPattern("yyyy-MM-dd")
        .format(this.getCreationDate());
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

  @Override
  public int hashCode() {
    int hash = 3;
    hash = 23 * hash + Objects.hashCode(this.url);
    return hash;
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
}
