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
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("tags", tagRepository.findAll());
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
        return "home";
    }

    @PostMapping(path = "/pet/form/add")
    @ResponseBody
    public ResponseEntity<Pet> addPet(@RequestParam Long category_id_add, @RequestParam String name, @RequestParam(value = "tag_id_list_add") String[] tag_id_list_add) {
        Set<Tag> newTagList = new HashSet<>();
        for (String tag_id : tag_id_list_add) {
            newTagList.add((tagRepository.findById(Long.valueOf(tag_id))).get());
        }
        return ResponseEntity.ok(petService.addPet(Pet.builder().
                category((categoryRepository.findById(category_id_add)).get()).
                name(name).
                tagSet(newTagList).
                status(PetStatus.AVAILABLE).
                build()));
    }

    @GetMapping(path = "/pet/form/getbyid")
    @ResponseBody
    public ResponseEntity<Pet> getPetById(@RequestParam Long id) {
        Optional<Pet> result = petService.getPetById(id);
        if (result.isPresent()) {
            return ResponseEntity.ok(petService.getPetById(id).get());
        } else return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/pet/form/getallhtml")
    public String getAllPetsHTML(Model model) {
        List<Pet> allPets = new ArrayList<>();
        for (Pet pet : petService.getAllPets()) {
            allPets.add(pet);
        }
        model.addAttribute("allPets", allPets);
        return ("pet_list");
    }

    @GetMapping(path = "/pet/form/getalljson")
    public ResponseEntity getAllPetsJSON() {
        return ResponseEntity.ok(petService.getAllPets());
    }

    @GetMapping(path = "/pet/form/update")
    @ResponseBody
    public ResponseEntity<Pet> updatePetWithForm(@RequestParam Long pet_id, @RequestParam Long category_id, @RequestParam String name, @RequestParam(value = "tag_id_list") String[] tag_id_list, @RequestParam String pet_status) {
        Optional<Pet> result = petService.getPetById(pet_id);
        if (result.isPresent()) {
            Pet updatedPet = result.get();
            updatedPet.setCategory(categoryRepository.findById(category_id).get());
            updatedPet.setName(name);
            Set<Tag> newTagSet = new HashSet<>();
            for (String tag_id : tag_id_list) {
                newTagSet.add((tagRepository.findById(Long.valueOf(tag_id))).get());
            }
            updatedPet.setTagSet(newTagSet);
            switch (pet_status) {
                case "AVAILABLE":
                    updatedPet.setStatus(PetStatus.AVAILABLE);
                    break;
                case "PENDING":
                    updatedPet.setStatus(PetStatus.PENDING);
                    break;
                case "SOLD":
                    updatedPet.setStatus(PetStatus.SOLD);
                    break;
            }
            return ResponseEntity.ok(petService.updatePetWithForm(pet_id, updatedPet).get());
        } else return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/pet/form/delete")
    @ResponseBody
    public void deletePet(@RequestParam(value = "pet_id_delete") Long[] pet_id_list_update) {
        for (Long pet_id : pet_id_list_update) {
            petService.deletePet(pet_id);
        }
    }

    @PostMapping(path = "/order/form/add")
    @ResponseBody
    public ResponseEntity<Order> addOrder(@RequestParam Long pet_id_add_order, @RequestParam Integer quantity, @RequestParam LocalDate ship_date) {
        // Get ordered pet
        Pet orderedPet = (petService.getPetById(pet_id_add_order)).get();
        // Build new order
        Order newOrder = Order.builder().pet(orderedPet).quantity(quantity).shipDate(ship_date).status(OrderStatus.PLACED).complete(Boolean.FALSE).build();
        // Set order on ordered pet
        orderedPet.setOrder(newOrder);
        // Set status on ordered pet
        orderedPet.setStatus(PetStatus.PENDING);
        // Save order & pet
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
        return ("order_list");
    }

    @GetMapping(path = "/order/form/getalljson")
    public ResponseEntity getAllOrdersJSON() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping(path = "/order/form/update")
    @ResponseBody
    public ResponseEntity<Order> updateOrderWithForm(@RequestParam Long order_id_update_order, @RequestParam String order_status_update_order, @RequestParam String order_iscomplete_update_order) {
        // Get order to update
        Order updatedOrder = (orderService.getOrderById(new Long(order_id_update_order))).get();
        // Set order status
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
        // Set order complete
        switch (order_iscomplete_update_order) {
            case "true":
                updatedOrder.setComplete(Boolean.TRUE);
                break;
            case "false":
                updatedOrder.setComplete(Boolean.FALSE);
                break;
        }
        // Send updated order to the DB
        Optional<Order> result = orderService.updateOrderWithForm(order_id_update_order, updatedOrder);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/order/form/delete")
    @ResponseBody
    public void deleteOrder(@RequestParam(value = "order_id_delete") Long[] order_id_list_delete) {
        for (Long order_id : order_id_list_delete) {
            orderService.deleteOrder(order_id);
        }
    }
}
