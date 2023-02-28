package com.example.SpringPetstore.controller;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

// TODO: Merge ViewController with PetStoreController
// TODO: Implement user registration
@Configuration
public class ViewController implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("template_home");
        registry.addViewController("/login").setViewName("template_login");
        registry.addViewController("/register").setViewName("template_register");
        registry.addViewController("/welcome").setViewName("template_welcome");
        registry.addViewController("/store").setViewName("template_store");
        registry.addViewController("/admin").setViewName("template_admin");
    }
}
