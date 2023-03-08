package com.example.SpringPetstore.controller;

import com.example.SpringPetstore.model.Order;
import com.example.SpringPetstore.model.OrderStatus;
import com.example.SpringPetstore.model.PetStatus;
import com.example.SpringPetstore.model.User;
import com.example.SpringPetstore.service.OrderService;
import com.example.SpringPetstore.service.PetService;
import com.example.SpringPetstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;

@Controller
public class StoreController {


    @Autowired
    OrderService orderService;
    PetService petService;
    UserService userService;

    public StoreController(OrderService orderService, PetService petService, UserService userService) {
        this.orderService = orderService;
        this.petService = petService;
        this.userService = userService;
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
        // TODO This won't work, use the update method of petService
        petService.getPetById(pet_id).get().setStatus(PetStatus.SOLD);
        return "Thank you for your order. Your new pet is already delivered to your home address. Please read the supplied instructions carefully. Go back to <a href=\"/store\">store</a>.";
    }

    @PostMapping(path = "/store/order/delete")
    public void deleteOrder(@RequestParam Long order_id) {
        // TODO
//        orderService.deleteOrder();
    }
}
