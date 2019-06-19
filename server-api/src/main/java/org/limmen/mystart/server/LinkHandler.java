package org.limmen.mystart.server;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.limmen.mystart.Link;
import org.limmen.mystart.Storage;
import org.limmen.mystart.criteria.Like;
import org.limmen.mystart.server.dto.LinkDto;

import spark.Request;
import spark.Response;

public class LinkHandler extends BaseHandler {
      
  public LinkHandler(Storage storage, ObjectMapper objectMapper) {
    super(storage, objectMapper);
  }

  private LinkDto toDto(Link link) {
    LinkDto dto = new LinkDto();
    dto.setId(link.getId());
    dto.setDescription(link.getDescription());
    dto.setLabels(link.getLabels().toArray(new String[link.getLabels().size()]));
    dto.setUrl(link.getUrl());
    dto.setTitle(link.getTitle());
    return dto;
  }

  public String search(Request req, Response res) {
    Long userId = 1L;
    String input = req.queryParams("input");
  
    return toJson(getLinkStorage().search(userId, List.of(
      new Like("description", input, String.class),
      new Like("title", input, String.class),
      new Like("url", input, String.class)
    )).stream().map(this::toDto).collect(Collectors.toList()));
  }

  public String byLabel(Request req, Response res) {
    Long userId = 1L;
    String label = req.queryParams("label");
  
    if (label != null && label.trim().length() > 0) {
      return toJson(getLinkStorage().getAllByLabel(userId, label).stream()
        .map(this::toDto)
        .collect(Collectors.toList()));
    } else {
      Map<String, Long> stats = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
      Collection<String> labels = getLinkStorage().getAllLabels(userId);
      Collection<Link> links = getLinkStorage().getAll(userId);
      labels.forEach(l -> {
        stats.put(l, links.stream().filter(f -> f.getLabels().contains(l)).count());
      });      
      return toJson(stats);
    }
  }

  public String delete(Request req, Response res) {
    Long userId = 1L;
    Long id = Long.valueOf(req.queryParams("id"));

    getLinkStorage().remove(userId, id);
    res.status(200);
    return "";
  }

  public String update(Request req, Response res) {
    Long userId = 1L;
    LinkDto link = fromJson(req.body(), LinkDto.class);    

    Link orig = getLinkStorage().get(userId, link.getId());

    orig.setLabels(Arrays.asList(link.getLabels()));
    orig.setDescription(link.getDescription());
    orig.setTitle(link.getTitle());
    orig.setUrl(link.getUrl());
    getLinkStorage().update(userId, orig);

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