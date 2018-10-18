package org.limmen.mystart;

import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import org.limmen.mystart.exception.StorageException;
import org.limmen.mystart.mystart.public_.ms_link.MsLink;
import org.limmen.mystart.mystart.public_.ms_link.MsLinkImpl;
import org.limmen.mystart.mystart.public_.ms_link.MsLinkManager;
import org.limmen.mystart.mystart.public_.ms_visits.MsVisits;
import org.limmen.mystart.mystart.public_.ms_visits.MsVisitsImpl;
import org.limmen.mystart.mystart.public_.ms_visits.MsVisitsManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbLinkStorage extends DbAbstractStorage implements LinkStorage {

  protected static final Logger LOGGER = LoggerFactory.getLogger(DbLinkStorage.class);

  private final MsLinkManager links;

  private final MsVisitsManager visits;

  public DbLinkStorage(MsLinkManager links, MsVisitsManager visits) {
    this.links = links;
    this.visits = visits;
  }

  private Link fromDb(MsLink link) {
    Link l = new Link(link.getUrl().get());

    l.setId(link.getId());
    l.setDescription(link.getDescription().orElse(null));
    if (link.getLabels().isPresent()) {
      l.setLabels(DomainUtil.parseLabels(link.getLabels().get()));
    }
    l.setPrivateNetwork(link.getPrivateNetwork().getAsBoolean());
    l.setTitle(link.getTitle().orElse(null));
    l.setSource(link.getSource().orElse(null));
    l.setCheckResult(link.getCheckResult().orElse(null));
    l.setCreationDate(date(link.getCreationDate()));
    l.setLastVisit(date(link.getLastVisit()));
    l.setLastCheck(date(link.getLastCheck()));

    return l;
  }

  @Override
  public Collection<Link> getAllByLabel(Long userId, String tagName) throws StorageException {
    return links.stream()
        .filter(MsLink.LABELS.containsIgnoreCase(tagName))
        .map(this::fromDb)
        .collect(Collectors.toList());
  }

  @Override
  public Collection<String> getAllLabels(Long userId) throws StorageException {
    Set<String> labels = new HashSet<>();

    this.links.stream().forEach((l -> {
      String lbls = l.getLabels().get();
      labels.addAll(Arrays.asList(lbls.split(";")));
    }));

    return labels.stream().sorted((c1, c2) -> {
      return c1.compareToIgnoreCase(c2);
    }).collect(Collectors.toList());
  }

  @Override
  public void importCollection(Long userId, Collection<Link> links, boolean skipDuplicates) throws StorageException {
    links.forEach(l -> {
      if (l.getUrl() == null) {
        LOGGER.info("Not adding link {} with title {} as it is empty.", l.getUrl(), l.getTitle());
      }
      Link link = getByUrl(userId, l.getUrl());
      if (link == null) {
        create(userId, l);
      } else {
        if (skipDuplicates) {
          LOGGER.info("Not adding link {} as we allready have it.", l.getUrl());
        } else {
          link.addLabels(l.getLabels());
          update(userId, link);
          LOGGER.info("Updating link {} by adding labels.", l.getUrl());
        }
      }
    });
  }

  @Override
  public void create(Long userId, Link link) {

    MsLink msLink = new MsLinkImpl();
    msLink.setUserId(userId);
    msLink.setDescription(link.getDescription());
    msLink.setLabels(link.getLabels().stream().reduce((acc, item) -> acc + ";" + item).get());
    msLink.setPrivateNetwork(link.isPrivateNetwork());
    msLink.setTitle(link.getTitle());
    msLink.setUrl(link.getUrl());
    msLink.setSource(link.getSource());
    msLink.setCheckResult(link.getCheckResult());
    msLink.setCreationDate(ts(link.getCreationDate()));
    msLink.setLastVisit(ts(link.getLastVisit()));
    msLink.setLastCheck(ts(link.getLastCheck()));

    links.persist(msLink);
  }

  @Override
  public void update(Long userId, Link link) throws StorageException {
    links.stream()
        .filter(MsLink.ID.equal(link.getId()))
        .filter(MsLink.USER_ID.equal(userId))
        .findFirst()
        .ifPresent(msLink -> {

          msLink.setTitle(link.getTitle());
          msLink.setUrl(link.getUrl());
          msLink.setPrivateNetwork(link.isPrivateNetwork());
      msLink.setLabels(link.getLabels().stream()
          .reduce((acc, item) -> acc + ";" + item).get());
      msLink.setDescription(link.getDescription());
      msLink.setCheckResult(link.getCheckResult());
      msLink.setLastCheck(ts(link.getLastCheck()));

      if (link.getLastVisit() != null) {
        Timestamp timestamp = ts(link.getLastVisit());
        msLink.setLastVisit(timestamp);
        visits.persist(new MsVisitsImpl()
            .setLinkId(link.getId())
            .setVisit(timestamp));
      }

      links.update(msLink);
        });
  }

  @Override
  public void remove(Long userId, Long id) throws StorageException {
    visits.stream()
        .filter(MsVisits.LINK_ID.equal(id))
        .forEach(visits.remover());

    links.stream()
        .filter(MsLink.ID.equal(id))
        .filter(MsLink.USER_ID.equal(userId))
        .forEach(links.remover());
  }

  public Link getByUrl(Long userId, String url) {
    String partUrl = url;
    if (url.startsWith("https://")) {
      partUrl = url.substring(8);
    } else if (url.startsWith("http://")) {
      partUrl = url.substring(7);
    }

    return links.stream()
        .filter(MsLink.URL.contains(partUrl))
        .filter(MsLink.USER_ID.equal(userId))
        .map(this::fromDb)
        .findFirst()
        .orElse(null);
  }

  @Override
  public Link get(Long userId, Long id) throws StorageException {
    return links.stream()
        .filter(MsLink.ID.equal(id))
        .filter(MsLink.USER_ID.equal(userId))
        .map(this::fromDb)
        .findFirst()
        .orElse(null);
  }

  @Override
  public Collection<Link> getAll(Long userId) throws StorageException {
    return links.stream().map(this::fromDb).collect(Collectors.toList());
  }
}
