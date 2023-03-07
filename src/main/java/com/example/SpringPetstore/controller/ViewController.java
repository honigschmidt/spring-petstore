package com.example.SpringPetstore.controller;

import com.example.SpringPetstore.model.*;
import com.example.SpringPetstore.service.OrderService;
import com.example.SpringPetstore.service.PetService;
import com.example.SpringPetstore.service.PhotoService;
import com.example.SpringPetstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.*;

@Controller
@Configuration
public class ViewController implements WebMvcConfigurer {

    @Autowired
    PetService petService;
    OrderService orderService;
    UserService userService;
    PhotoService photoService;
    CategoryRepository categoryRepository;
    TagRepository tagRepository;

    public ViewController(PetService petService, OrderService orderService, UserService userService, CategoryRepository categoryRepository, TagRepository tagRepository, PhotoService photoService) {
        this.petService = petService;
        this.orderService = orderService;
        this.userService = userService;
        this.photoService = photoService;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("template_home");
        registry.addViewController("/login").setViewName("template_login");
        registry.addViewController("/register").setViewName("template_register");
        registry.addViewController("/store").setViewName("template_store");
        registry.addViewController("/admin").setViewName("template_admin");
    }

    @GetMapping(path = "/")
    public String getHome(Model model) {
        Iterable<Pet> availablePets = petService.getPetsByStatus(PetStatus.AVAILABLE).get();
        Map<String, String> availablePetsPhotoMap = new HashMap<>();
        List<String> petPhotoList = new ArrayList<>();
        for (Pet pet : availablePets) {
            for (Photo photo : pet.getPhotoSet()) {
                petPhotoList.add(photo.getUrl());
            }
            if (!petPhotoList.isEmpty())
            availablePetsPhotoMap.put(pet.getName(), petPhotoList.get(0));
            petPhotoList.clear();
        }
        model.addAttribute("available_pet_list", availablePets);
        model.addAttribute("pet_photo_list", availablePetsPhotoMap);
        return "template_home";
    }

    @GetMapping(path = "/admin")
    public String getAdmin(Model model) {
        model.addAttribute("category_list", categoryRepository.findAll());
        model.addAttribute("tag_list", tagRepository.findAll());
        model.addAttribute("pet_status_list", PetStatus.getPetStatusList());
        model.addAttribute("pet_list", petService.getAllPets());
        model.addAttribute("order_status_list", OrderStatus.getOrderStatusList());
        model.addAttribute("order_list", orderService.getAllOrders());

        List<Pet> availablePets = new ArrayList<>();
        for (Pet pet : petService.getAllPets()) {
            if (pet.getStatus().equals(PetStatus.AVAILABLE)) {
                availablePets.add(pet);
            }
        }
        model.addAttribute("available_pet_list", availablePets);

        model.addAttribute("user_list", userService.getAllUsers());
        model.addAttribute("photo_list", photoService.getAllPhotos());
        return "template_admin";
    }

    @GetMapping(path = "/store")
    public String getStore(Model model) {
        Iterable<Pet> availablePets = petService.getPetsByStatus(PetStatus.AVAILABLE).get();
        Map<String, String> availablePetsPhotoMap = new HashMap<>();
        List<String> petPhotoList = new ArrayList<>();
        for (Pet pet : availablePets) {
            for (Photo photo : pet.getPhotoSet()) {
                petPhotoList.add(photo.getUrl());
            }
            if (!petPhotoList.isEmpty())
                availablePetsPhotoMap.put(pet.getName(), petPhotoList.get(0));
            petPhotoList.clear();
        }
        model.addAttribute("available_pet_list", availablePets);
        model.addAttribute("pet_photo_list", availablePetsPhotoMap);
        return "template_store";
    }

    @GetMapping(path = "/register")
    public String getRegister(Model model) {
        return "template_register";
    }
}
