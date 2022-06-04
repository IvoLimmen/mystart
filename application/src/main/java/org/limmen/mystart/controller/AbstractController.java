package org.limmen.mystart.controller;

import org.limmen.mystart.Link;
import org.limmen.mystart.dto.LinkDto;

import io.micronaut.security.authentication.Authentication;
import io.micronaut.security.utils.DefaultSecurityService;

public abstract class AbstractController {
    
    private final DefaultSecurityService defaultSecurityService;

    protected AbstractController(DefaultSecurityService defaultSecurityService) {
        this.defaultSecurityService = defaultSecurityService;
    }

    protected Long getUserId() {
        Authentication authentication = this.defaultSecurityService.getAuthentication().get();

        return (Long) authentication.getAttributes().get("id");
    }

    protected LinkDto toLinkDto(Link link) {
      LinkDto dto = new LinkDto();
      dto.setId(link.getId());
      dto.setDescription(link.getDescription());
      dto.setLabels(link.getLabels().toArray(new String[link.getLabels().size()]));
      dto.setUrl(link.getUrl());
      dto.setTitle(link.getTitle());
      return dto;
    }
}
