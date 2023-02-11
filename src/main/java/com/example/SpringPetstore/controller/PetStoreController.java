package com.example.SpringPetstore.controller;

import com.example.SpringPetstore.model.*;
import com.example.SpringPetstore.service.OrderService;
import com.example.SpringPetstore.service.PetService;
import com.example.SpringPetstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.util.*;

@Controller
public class PetStoreController {

    @Autowired
    PetService petService;
    OrderService orderService;
    UserService userService;
    CategoryRepository categoryRepository;
    TagRepository tagRepository;

    public PetStoreController(PetService petService, OrderService orderService, UserService userService, CategoryRepository categoryRepository, TagRepository tagRepository) {
        this.petService = petService;
        this.orderService = orderService;
        this.userService = userService;
        this.categoryRepository = categoryRepository;
        this.tagRepository = tagRepository;
    }

    @GetMapping(path = "/")
    public String getHome(Model model) {
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
        return "template_home";
    }

    @PostMapping(path = "/order/form/add")
    @ResponseBody
    public ResponseEntity<Order> addOrder(@RequestParam Long pet_id_add_order, @RequestParam Integer quantity, @RequestParam LocalDate ship_date) {
        Pet orderedPet = (petService.getPetById(pet_id_add_order)).get();
        Order newOrder = Order.builder().pet(orderedPet).quantity(quantity).shipDate(ship_date).status(OrderStatus.PLACED).complete(Boolean.FALSE).build();
        orderedPet.setOrder(newOrder);
        orderedPet.setStatus(PetStatus.PENDING);
        return ResponseEntity.ok(orderService.addOrder(newOrder));
        }

    @GetMapping(path = "/order/form/getbyid")
    @ResponseBody
    public ResponseEntity<Order> getOrderById(@RequestParam Long id) {
        Optional<Order> result = orderService.getOrderById(id);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/order/form/getallhtml")
    public String getAllOrdersHTML(Model model) {
        model.addAttribute("allOrders", orderService.getAllOrders());
        return ("template_order_list");
    }

    @GetMapping(path = "/order/form/getalljson")
    public ResponseEntity getAllOrdersJSON() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping(path = "/order/form/update")
    @ResponseBody
    public ResponseEntity<Order> updateOrderWithForm(@RequestParam Long order_id_update_order, @RequestParam String order_status_update_order, @RequestParam String order_iscomplete_update_order) {
        Order updatedOrder = (orderService.getOrderById(order_id_update_order)).get();
        switch (order_status_update_order) {
            case "DUMMY":
                updatedOrder.setStatus(OrderStatus.DUMMY);
                break;
            case "PLACED":
                updatedOrder.setStatus(OrderStatus.PLACED);
                break;
            case "APPROVED":
                updatedOrder.setStatus(OrderStatus.APPROVED);
                break;
            case "DELIVERED":
                updatedOrder.setStatus(OrderStatus.DELIVERED);
                break;
        }
        switch (order_iscomplete_update_order) {
            case "true":
                updatedOrder.setComplete(Boolean.TRUE);
                break;
            case "false":
                updatedOrder.setComplete(Boolean.FALSE);
                break;
        }
        Optional<Order> result = orderService.updateOrderWithForm(order_id_update_order, updatedOrder);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/order/form/delete")
    public void deleteOrder(@RequestParam(value = "order_id") Long[] order_id_list) {
        for (Long order_id : order_id_list) {
            orderService.deleteOrder(order_id);
        }
    }
}
