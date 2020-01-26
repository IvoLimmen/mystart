package org.limmen.mystart.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.StatsStorage;
import org.limmen.mystart.Storage;
import org.limmen.mystart.UserStorage;
import org.limmen.mystart.VisitStorage;

@Slf4j
@Getter
public abstract class BaseHandler {

  private final LinkStorage linkStorage;
  private final UserStorage userStorage;
  private final VisitStorage visitStorage;
  private final StatsStorage statsStorage;
  private final ObjectMapper objectMapper;

  public BaseHandler(final Storage storage, final ObjectMapper objectMapper) {
    this.linkStorage = storage.getLinkStorage();
    this.userStorage = storage.getUserStorage();
    this.visitStorage = storage.getVisitStorage();
    this.statsStorage = storage.getStatsStorage();
    this.objectMapper = objectMapper;
  }

  protected <T> T fromJson(String json, Class<T> clazz) {
    try {
      return objectMapper.readValue(json, clazz);    
    } catch (Exception e) {
      log.error("Error while reading JSON", e);
      return null;
    }
  }

  protected String toJson(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (Exception e) {
      log.error("Error while writing object to JSON", e);
      return null;
    }
  }
}