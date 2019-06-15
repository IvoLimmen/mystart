package org.limmen.mystart.server;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.limmen.mystart.Link;
import org.limmen.mystart.Storage;
import org.limmen.mystart.criteria.Like;

import spark.Request;
import spark.Response;

public class LinkHandler extends BaseHandler {
      
  public LinkHandler(Storage storage, ObjectMapper objectMapper) {
    super(storage, objectMapper);
  }

  public String search(Request req, Response res) {
    Long userId = 1L;
    String input = req.queryParams("input");
    
    return toJson(getLinkStorage().search(userId, List.of(
      new Like("description", input, String.class),
      new Like("title", input, String.class),
      new Like("url", input, String.class)
    )));
  }

  public String delete(Request req, Response res) {
    Long userId = 1L;
    Long id = Long.valueOf(req.queryParams("id"));

    getLinkStorage().remove(userId, id);
    res.status(200);
    return "";
  }

  public String visit(Request req, Response res) {
    Long userId = 1L;
    Long id = Long.valueOf(req.queryParams("id"));

    Link link = getLinkStorage().get(userId, id);
    link.visited();
    getLinkStorage().update(userId, link);
    getVisitStorage().visit(link.getId());
    
    res.status(200);
    return "";
  }
}