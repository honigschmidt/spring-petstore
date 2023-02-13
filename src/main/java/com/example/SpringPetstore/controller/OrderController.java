package com.example.SpringPetstore.controller;

import com.example.SpringPetstore.model.Order;
import com.example.SpringPetstore.model.OrderStatus;
import com.example.SpringPetstore.model.Pet;
import com.example.SpringPetstore.model.PetStatus;
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
import java.util.Optional;

@Controller
public class OrderController {

    @Autowired
    OrderService orderService;
    PetService petService;

    public OrderController(OrderService orderService, PetService petService) {
        this.orderService = orderService;
        this.petService = petService;
    }

    @PostMapping(path = "/order/form/add")
    @ResponseBody
    public ResponseEntity<Order> addOrder(@RequestParam Long pet_id, @RequestParam Integer quantity, @RequestParam LocalDate ship_date) {
        Pet orderedPet = (petService.getPetById(pet_id)).get();
        Order newOrder = Order.builder().
                pet(orderedPet).
                quantity(quantity).
                shipDate(ship_date).
                status(OrderStatus.PLACED).
                complete(Boolean.FALSE).
                build();
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
        model.addAttribute("order_list", orderService.getAllOrders());
        return ("template_order_list");
    }

    @GetMapping(path = "/order/form/getalljson")
    public ResponseEntity getAllOrdersJSON() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping(path = "/order/form/update")
    @ResponseBody
    public ResponseEntity<Order> updateOrderWithForm(@RequestParam Long order_id, @RequestParam String order_status, @RequestParam String order_complete) {
        Optional<Order> result = orderService.getOrderById(order_id);
        if (result.isPresent()) {
            Order updatedOrder = result.get();
            switch (order_status) {
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
            switch (order_complete) {
                case "true":
                    updatedOrder.setComplete(Boolean.TRUE);
                    break;
                case "false":
                    updatedOrder.setComplete(Boolean.FALSE);
                    break;
            }
            return ResponseEntity.ok(orderService.updateOrderWithForm(order_id, updatedOrder).get());
        } else return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/order/form/delete")
    public void deleteOrder(@RequestParam(value = "order_id") Long[] order_id_list) {
        for (Long order_id : order_id_list) {
            orderService.deleteOrder(order_id);
        }
    }
}
