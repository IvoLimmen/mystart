package org.limmen.mystart.controller;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.limmen.mystart.Link;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.dto.LabelDto;
import org.limmen.mystart.dto.LinkDto;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.utils.DefaultSecurityService;
import jakarta.inject.Inject;

@Controller("/api/label")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class LabelController extends AbstractController {

  private LinkStorage linkStorage;

  @Inject
  public LabelController(DefaultSecurityService defaultSecurityService, LinkStorage linkStorage) {
    super(defaultSecurityService);
    this.linkStorage = linkStorage;
  }

  @Get("/")
  public Collection<LabelDto> all() {
    Set<LabelDto> labelDtos = new TreeSet<>();
    Collection<String> labels = this.linkStorage.getAllLabels(getUserId());
    Collection<Link> links = this.linkStorage.getAll(getUserId());
    labels.forEach(l -> {
      labelDtos.add(new LabelDto(l, links.stream().filter(f -> f.getLabels().contains(l)).count()));
    });
    return labelDtos;
  }

  @Get("/{label}")
  public List<LinkDto> byLabel(@PathVariable String label) {
    if (label != null && label.trim().length() > 0) {
      return this.linkStorage.getAllByLabel(getUserId(), label).stream()
          .map(this::toLinkDto)
          .collect(Collectors.toList());
    } else {
      return Collections.emptyList();
    }
  }

}
