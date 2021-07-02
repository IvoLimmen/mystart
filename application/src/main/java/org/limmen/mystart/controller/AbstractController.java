package org.limmen.mystart.controller;

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
}
