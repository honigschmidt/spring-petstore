package com.example.SpringPetstore.controller.view;

import com.example.SpringPetstore.model.*;
import com.example.SpringPetstore.service.OrderService;
import com.example.SpringPetstore.service.PetService;
import com.example.SpringPetstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class StoreController {


    @Autowired
    OrderService orderService;
    PetService petService;
    UserService userService;

    Iterable<Pet> availablePets = new ArrayList<>();
    Map<String, String> availablePetsPhotoMap = new HashMap<>();
    List<String> petPhotoList = new ArrayList<>();
    Iterable<Order> userOrderList = new ArrayList<>();

    public StoreController(OrderService orderService, PetService petService, UserService userService) {
        this.orderService = orderService;
        this.petService = petService;
        this.userService = userService;
    }

    public void refreshModelAttributes(String loggedInUserName) {
        availablePets = petService.getPetsByStatus(PetStatus.AVAILABLE).get();
        for (Pet pet : availablePets) {
            for (Photo photo : pet.getPhotoSet()) {
                petPhotoList.add(photo.getUrl());
            }
            if (!petPhotoList.isEmpty())
                availablePetsPhotoMap.put(pet.getName(), petPhotoList.get(0));
            petPhotoList.clear();
        }
        userOrderList = orderService.getOrderByUserId(userService.getUserByUsername(loggedInUserName).get().getId()).get();
    }

    @GetMapping(path = "/store")
    public String getStoreView(@CurrentSecurityContext(expression = "authentication?.name")
                               String loggedInUserName, Model model) {
        refreshModelAttributes(loggedInUserName);
        model.addAttribute("available_pet_list", availablePets);
        model.addAttribute("pet_photo_list", availablePetsPhotoMap);
        model.addAttribute("user_order_list", userOrderList);
        return "template_store";
    }

    @PostMapping(path = "/store/order/add")
    @ResponseBody
    public String addOrder(@CurrentSecurityContext(expression = "authentication?.name")
                           String loggedInUserName, @RequestParam Long pet_id) {
        orderService.addOrder(Order.builder()
                .pet(petService.getPetById(pet_id).get())
                .quantity(1)
                .shipDate(LocalDate.now())
                .status(OrderStatus.DELIVERED)
                .complete(Boolean.FALSE)
                .user(userService.getUserByUsername(loggedInUserName).get())
                .build());
        Pet updatedPet = petService.getPetById(pet_id).get();
        updatedPet.setStatus(PetStatus.SOLD);
        petService.updatePetWithForm(updatedPet.getId(), updatedPet);
        return "Thank you for your order. Your new pet is already delivered to your home address. Please read the supplied instructions carefully. Go back to <a href=\"/store\">store</a>.";
    }

    @PostMapping(path = "/store/order/delete")
    @ResponseBody
    public String deleteOrder(@RequestParam Long order_id, @RequestParam Long pet_id) {
        orderService.deleteOrder(order_id);
        Pet updatedPet = petService.getPetById(pet_id).get();
        updatedPet.setStatus(PetStatus.AVAILABLE);
        petService.updatePetWithForm(pet_id, updatedPet);
        return "Order deleted. Go back to <a href=\"/store\">store</a>.";
    }
}
