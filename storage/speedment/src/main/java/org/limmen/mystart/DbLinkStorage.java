package org.limmen.mystart;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.limmen.mystart.exception.StorageException;
import org.limmen.mystart.mystart.public_.ms_link.MsLink;
import org.limmen.mystart.mystart.public_.ms_link.MsLinkImpl;
import org.limmen.mystart.mystart.public_.ms_link.MsLinkManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbLinkStorage implements LinkStorage {

  protected static final Logger LOGGER = LoggerFactory.getLogger(DbLinkStorage.class);

  private final MsLinkManager links;

  public DbLinkStorage(MsLinkManager links) {
    this.links = links;
  }

  private Link fromDb(MsLink link) {
    Link l = new Link(link.getUrl().get());

    l.setHost(link.getHost().orElse(null));
    l.setId((long) link.getId());
    l.setDescription(link.getDescription().orElse(null));
    if (link.getLabels().isPresent()) {
      String label = link.getLabels().get();
      String[] labels = label.split(";");
      for (int i = 0; i < labels.length; i++) {
        labels[i] = labels[i].trim();
      }
      l.setLabels(Arrays.asList(labels));
    }
    l.setPrivateNetwork(link.getPrivateNetwork().getAsBoolean());
    l.setTitle(link.getTitle().orElse(null));
    l.setSource(link.getSource().orElse(null));
    l.setCreationDate(date(link.getCreationDate()));
    l.setLastVisit(date(link.getLastVisit()));

    return l;
  }

  private LocalDateTime date(Optional<Timestamp> ts) {
    if (!ts.isPresent()) {
      return null;
    } else {
      return ts.get().toLocalDateTime();
    }
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
  public void createCollection(Long userId, Collection<Link> link) throws StorageException {
    link.forEach(l -> {
      try {
        if (l.getUrl() == null) {
          LOGGER.info("Not adding link {} with title {} as it is empty.", l.getUrl(), l.getTitle());
        }
        if (getByUrl(userId, l.getUrl()) == null) {
          create(userId, l);
        } else {
          LOGGER.info("Not adding link {} as we allready have it.", l.getUrl());
        }
      } catch (StorageException ex) {
        LOGGER.warn("Not adding link {} as we could not check the existance.", l.getUrl());
      }
    });
  }

  @Override
  public void create(Long userId, Link link) throws StorageException {

    MsLink msLink = new MsLinkImpl();
    msLink.setUserId(userId.intValue());
    msLink.setDescription(link.getDescription());
    msLink.setHost(link.getHost());
    msLink.setLabels(link.getLabels().stream().reduce((acc, item) -> acc + ";" + item).get());
    msLink.setPrivateNetwork(link.isPrivateNetwork());
    msLink.setTitle(link.getTitle());
    msLink.setUrl(link.getUrl());
    msLink.setSource(link.getSource());
    msLink.setCreationDate(ts(link.getCreationDate()));
    msLink.setLastVisit(ts(link.getLastVisit()));

    links.persist(msLink);
  }

  @Override
  public void update(Long userId, Link link) throws StorageException {
    links.stream()
        .filter(MsLink.ID.equal(link.getId().intValue()))
        .filter(MsLink.USER_ID.equal(userId.intValue()))
        .map(MsLink.DESCRIPTION.setTo(link.getDescription()))
        .map(MsLink.LABELS.setTo(link.getLabels().stream().reduce((acc, item) -> acc + ";" + item).get()))
        .map(MsLink.LAST_VISIT.setTo(ts(link.getLastVisit())))
        .map(MsLink.PRIVATE_NETWORK.setTo(link.isPrivateNetwork()))
        .map(MsLink.TITLE.setTo(link.getTitle()))
        .forEach(links.updater());
  }

  private Timestamp ts(LocalDateTime date) {
    if (date == null) {
      return null;
    } else {
      return Timestamp.valueOf(date);
    }
  }

  @Override
  public void remove(Long userId, Long id) throws StorageException {
    Optional<MsLink> current = links.stream()
        .filter(MsLink.ID.equal(id.intValue()))
        .filter(MsLink.USER_ID.equal(userId.intValue()))
        .findFirst();

    current.ifPresent(l -> links.remove(l));
  }

  public Link getByUrl(Long userId, String url) throws StorageException {
    return links.stream()
        .filter(MsLink.URL.equal(url))
        .filter(MsLink.USER_ID.equal(userId.intValue()))
        .map(this::fromDb)
        .findFirst()
        .orElse(null);
  }

  @Override
  public Link get(Long userId, Long id) throws StorageException {
    return links.stream()
        .filter(MsLink.ID.equal(id.intValue()))
        .filter(MsLink.USER_ID.equal(userId.intValue()))
        .map(this::fromDb)
        .findFirst()
        .orElse(null);
  }

  @Override
  public Collection<Link> getAll(Long userId) throws StorageException {
    return links.stream().map(this::fromDb).collect(Collectors.toList());
  }
}
