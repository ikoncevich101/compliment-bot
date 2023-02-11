package com.darya.compiment.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/version")
public class VersionController {
    @Value("${application.version}")
    private String applicationVersion;

    @GetMapping
    public String getApplicationConfigurations() {
        return applicationVersion;
    }

}
