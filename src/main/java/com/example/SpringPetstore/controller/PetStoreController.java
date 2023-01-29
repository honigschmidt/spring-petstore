package com.example.SpringPetstore.controller;

import com.example.SpringPetstore.model.OrderStatus;
import com.example.SpringPetstore.model.PetStatus;
import com.example.SpringPetstore.view.JsonPet;
import com.example.SpringPetstore.model.Order;
import com.example.SpringPetstore.model.Pet;
import com.example.SpringPetstore.service.OrderService;
import com.example.SpringPetstore.service.PetService;
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

    public PetStoreController(PetService petService, OrderService orderService) {
        this.petService = petService;
        this.orderService = orderService;
    }

    @GetMapping(path = "/")
    public String getHome() {
        return "home";
    }

    // TODO: Add list of available tags and statuses to the form
    @PostMapping(path = "/pet/form/add")
    @ResponseBody
    public ResponseEntity<Pet> addPet(@RequestParam String name) {
        return ResponseEntity.ok(petService.addPet(Pet.builder().name(name).status(PetStatus.AVAILABLE).build()));
    }

    @GetMapping(path = "/pet/form/getbyid")
    @ResponseBody
    public ResponseEntity<Pet> getPetById(@RequestParam Long id) {
        Optional<Pet> result = petService.getPetById(id);
        if (result.isPresent()) {
            return ResponseEntity.ok(petService.getPetById(id).get());
        } else return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/pet/form/getall")
    public String getAllPets(Model model) {
        List<Pet> allPets = new ArrayList<>();
        for (Pet pet : petService.getAllPets()) {
            allPets.add(pet);
        }
        model.addAttribute("allPets", allPets);
        return ("pet_list");
    }

    @GetMapping(path = "/pet/form/update")
    @ResponseBody
    public ResponseEntity<Pet> updatePetWithForm(@RequestParam Long id, @RequestParam String name, @RequestParam String status) {
        Pet pet = Pet.builder().name(name).build();
        Optional<Pet> result = petService.updatePetWithForm(id, pet);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/pet/form/delete")
    @ResponseBody
    public String deletePet(@RequestParam Long id) {
        return petService.deletePet(id);
    }


    @PostMapping(path = "/order/form/add")
    @ResponseBody
    public void Order (@RequestParam Long petId, @RequestParam Integer quantity, @RequestParam LocalDate ship_date) {
        Optional<Pet> orderedPet = petService.getPetById(petId);
        if (orderedPet.isPresent()) {
            Order newOrder = orderService.addOrder(Order.builder().pet(orderedPet.get()).quantity(quantity).shipDate(ship_date).status(OrderStatus.DUMMY).complete(false).build());
        }
    }

    @GetMapping(path = "/order/form/getbyid")
    @ResponseBody
    public ResponseEntity<Order> getOrderById(@RequestParam Long id) {
        Optional<Order> result = orderService.getOrderById(id);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/order/form/getall")
    public String getAllOrders(Model model) {
        model.addAttribute("allOrders", orderService.getAllOrders());
        return ("order_list");
    }

//    @GetMapping(path = "/order/form/update")
//    @ResponseBody
//    public ResponseEntity<Order> updateOrderWithForm(@RequestParam Long id, @RequestParam Integer quantity, @RequestParam LocalDate ship_date, @RequestParam String status, @RequestParam Boolean complete) {
//        Order order = Order.builder().quantity(quantity).shipDate(ship_date).status(status).complete(complete).build();
//        Optional<Order> result = orderService.updateOrderWithForm(id, order);
//        if (result.isPresent()) {
//            return ResponseEntity.ok(result.get());
//        } else return ResponseEntity.notFound().build();
//    }

    @GetMapping(path = "/order/form/delete")
    @ResponseBody
    public String deleteOrder(@RequestParam Long id) {
        return orderService.deleteOrder(id);
    }

}
