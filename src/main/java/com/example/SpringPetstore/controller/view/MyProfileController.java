package com.example.SpringPetstore.controller.view;

import com.example.SpringPetstore.model.Pet;
import com.example.SpringPetstore.model.PetStatus;
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

@Controller
public class MyProfileController {

    @Autowired
    OrderService orderService;
    UserService userService;
    PetService petService;

    public MyProfileController(OrderService orderService, UserService userService, PetService petService) {
        this.orderService = orderService;
        this.userService = userService;
        this.petService = petService;
    }

    @GetMapping(path = "/myprofile")
    public String getView(@CurrentSecurityContext(expression = "authentication?.name")
                                      String loggedInUserName, Model model) {
        // Add template attributes
        model.addAttribute("user_profile_data", userService.getUserByUsername(loggedInUserName).get());
        model.addAttribute("user_order_list", orderService.getOrderByUserId(userService.getUserByUsername(loggedInUserName).get().getId()));
        return "template_my_profile";
    }

    @PostMapping(path = "/myprofile/order/delete")
    @ResponseBody
    public String deleteOrder(@RequestParam Long order_id, @RequestParam Long pet_id) {
        orderService.deleteOrder(order_id);
        Pet updatedPet = petService.getPetById(pet_id).get();
        updatedPet.setStatus(PetStatus.AVAILABLE);
        petService.updatePetWithForm(updatedPet);
        // TODO: Use the messagebox template here
        return "Order deleted. Go back to <a href=\"/store\">store</a>.";
    }
}
