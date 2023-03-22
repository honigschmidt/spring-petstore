package com.example.SpringPetstore.controller.view;

import com.example.SpringPetstore.model.CategoryRepository;
import com.example.SpringPetstore.model.OrderStatus;
import com.example.SpringPetstore.model.PetStatus;
import com.example.SpringPetstore.model.TagRepository;
import com.example.SpringPetstore.service.OrderService;
import com.example.SpringPetstore.service.PetService;
import com.example.SpringPetstore.service.PhotoService;
import com.example.SpringPetstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

    @Autowired
    PetService petService;
    OrderService orderService;
    UserService userService;
    PhotoService photoService;
    CategoryRepository categoryRepository;
    TagRepository tagRepository;

    public AdminController(PetService petService, OrderService orderService, UserService userService, CategoryRepository categoryRepository, TagRepository tagRepository, PhotoService photoService) {
        this.petService = petService;
        this.orderService = orderService;
        this.userService = userService;
        this.photoService = photoService;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    @GetMapping(path = "/admin")
    public String getView(Model model) {
        model.addAttribute("category_list", categoryRepository.findAll());
        model.addAttribute("tag_list", tagRepository.findAll());
        model.addAttribute("pet_status_list", PetStatus.getPetStatusList());
        model.addAttribute("pet_list", petService.getAllPets());
        model.addAttribute("order_status_list", OrderStatus.getOrderStatusList());
        model.addAttribute("order_list", orderService.getAllOrders());
        model.addAttribute("available_pet_list", petService.getPetsByStatus(PetStatus.AVAILABLE).get());
        model.addAttribute("user_list", userService.getAllUsers());
        model.addAttribute("photo_list", photoService.getAllPhotos());
        return "template_admin";
    }
}
