package org.limmen.mystart.controller;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.limmen.mystart.Link;
import org.limmen.mystart.LinkStorage;
import org.limmen.mystart.VisitStorage;
import org.limmen.mystart.criteria.Criteria;
import org.limmen.mystart.criteria.Like;
import org.limmen.mystart.criteria.Or;
import org.limmen.mystart.dto.LinkDto;

import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Delete;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import io.micronaut.http.annotation.Put;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.security.annotation.Secured;
import io.micronaut.security.rules.SecurityRule;
import io.micronaut.security.utils.DefaultSecurityService;
import jakarta.inject.Inject;

@Controller("/api/link")
@Secured(SecurityRule.IS_AUTHENTICATED)
public class LinkController extends AbstractController {

    private VisitStorage visitStorage;

    private LinkStorage linkStorage;

    @Inject
    public LinkController(DefaultSecurityService defaultSecurityService, VisitStorage visitStorage, LinkStorage linkStorage) {
        super(defaultSecurityService);
        this.visitStorage = visitStorage;
        this.linkStorage = linkStorage;        
    }

    private LinkDto toLinkDto(Link link) {
        LinkDto dto = new LinkDto();
        dto.setId(link.getId());
        dto.setDescription(link.getDescription());
        dto.setLabels(link.getLabels().toArray(new String[link.getLabels().size()]));
        dto.setUrl(link.getUrl());
        dto.setTitle(link.getTitle());
        return dto;
    }

    @Get("/search")
    public List<LinkDto> search(@QueryValue String input) {
        Criteria criteria = new Or(new Like("description", input, String.class),
                new Or(new Like("title", input, String.class), new Like("url", input, String.class)));

        return this.linkStorage.search(getUserId(), criteria).stream()
            .map(this::toLinkDto)
            .collect(Collectors.toList());
    }

    @Get("/by_label/{label}")
    public List<LinkDto> byLabel(@PathVariable String label) {

        if (label != null && label.trim().length() > 0) {
            return this.linkStorage.getAllByLabel(getUserId(), label).stream()
                .map(this::toLinkDto)
                .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Delete("/delete/{id}")
    public void delete(@PathVariable Long id) {
        this.linkStorage.remove(getUserId(), id);
    }

    @Put("/update")
    public void update(@Body LinkDto link) {

        Link orig = this.linkStorage.get(getUserId(), link.getId()).orElseThrow();

        orig.setLabels(Arrays.asList(link.getLabels()));
        orig.setDescription(link.getDescription());
        orig.setTitle(link.getTitle());
        orig.setUrl(link.getUrl());
        this.linkStorage.update(getUserId(), orig);
    }

    @Put("/visit/{id}")
    public void visit(@PathVariable Long id) {
        Link link = this.linkStorage.get(getUserId(), id).orElseThrow();
        link.visited();
        this.linkStorage.update(getUserId(), link);
        this.visitStorage.visit(link.getId());
    }
}
