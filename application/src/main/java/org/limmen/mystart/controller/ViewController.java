package org.limmen.mystart.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.views.View;

@Controller("/")
public class ViewController {
    
    @View("index")
    @Get("/")
    public HttpResponse<?> index() {
        return HttpResponse.ok();
    }

}
