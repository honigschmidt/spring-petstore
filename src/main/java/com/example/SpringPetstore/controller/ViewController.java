package com.example.SpringPetstore.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ViewController implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("template_home");
        registry.addViewController("/welcome").setViewName("template_welcome");
        registry.addViewController("/store").setViewName("template_store");
        registry.addViewController("/admin").setViewName("template_admin");
        registry.addViewController("/login").setViewName("template_login");
    }
}
