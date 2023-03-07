package com.example.SpringPetstore.controller;

import com.example.SpringPetstore.service.OrderService;
import com.example.SpringPetstore.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StoreController {


    @Autowired
    OrderService orderService;
    PetService petService;

    public StoreController(OrderService orderService, PetService petService) {
        this.orderService = orderService;
        this.petService = petService;
    }

    @PostMapping(path = "/store/create")
    public void createOrder(@RequestParam Long pet_id) {
        // TODO
//        orderService.addOrder();
    }

    @PostMapping(path = "/store/cancel")
    public void cancelOrder(@RequestParam Long order_id) {
        // TODO
//        orderService.deleteOrder();
    }
}
