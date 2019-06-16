package org.limmen.mystart.server;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.StatsStorage;
import org.limmen.mystart.Storage;
import org.limmen.mystart.UserStorage;
import org.limmen.mystart.VisitStorage;

import lombok.Getter;

@Getter
public abstract class BaseHandler {

  private LinkStorage linkStorage;
  private UserStorage userStorage;
  private VisitStorage visitStorage;
  private StatsStorage statsStorage;
  private ObjectMapper objectMapper;

  public BaseHandler(Storage storage, ObjectMapper objectMapper) {
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
      return null;
    }
  }

  protected String toJson(Object object) {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (Exception e) {
      return e.getMessage();
    }
  }
}