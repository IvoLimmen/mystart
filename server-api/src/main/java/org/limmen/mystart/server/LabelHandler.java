package org.limmen.mystart.server;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.limmen.mystart.Link;
import org.limmen.mystart.Storage;
import org.limmen.mystart.server.dto.LabelDto;

import spark.Request;
import spark.Response;

public class LabelHandler extends BaseHandler {

  public LabelHandler(Storage storage, ObjectMapper objectMapper) {
    super(storage, objectMapper);
  }

  public String all(Request req, Response res) {
    Long userId = 1L;
  
    Set<LabelDto> labelDtos = new TreeSet<>();
    Collection<String> labels = getLinkStorage().getAllLabels(userId);
    Collection<Link> links = getLinkStorage().getAll(userId);
    labels.forEach(l -> {
      labelDtos.add(new LabelDto(l, links.stream().filter(f -> f.getLabels().contains(l)).count()));
    });      
    return toJson(labelDtos);
  }
}