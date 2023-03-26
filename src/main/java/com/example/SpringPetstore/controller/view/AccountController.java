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
public class AccountController {

    @Autowired
    OrderService orderService;
    UserService userService;
    PetService petService;

    public AccountController(OrderService orderService, UserService userService, PetService petService) {
        this.orderService = orderService;
        this.userService = userService;
        this.petService = petService;
    }

    @GetMapping(path = "/account")
    public String getView(@CurrentSecurityContext(expression = "authentication?.name") String loggedInUserName, Model model) {
        model.addAttribute("user", userService.getUserByUsername(loggedInUserName).get());
        model.addAttribute("orders", orderService.getOrderByUserId(userService.getUserByUsername(loggedInUserName).get().getId()).get());
        return "template_account";
    }

    // TODO: PW change
    @PostMapping(path = "/account/password/change")
    @ResponseBody
    public String changePassword (String loggedInUserPassword, @RequestParam Long user_id, @RequestParam String old_password, @RequestParam String new_password) {
        return null;
    }

    // TODO: Account deletion
    @PostMapping(path = "/account/order/delete")
    public String deleteOrder(@RequestParam Long order_id, @RequestParam Long pet_id, Model model) {
        orderService.deleteOrder(order_id);
        Pet updatedPet = petService.getPetById(pet_id).get();
        updatedPet.setStatus(PetStatus.AVAILABLE);
        petService.updatePetWithForm(updatedPet);
        model.addAttribute("image", "~/images/MessageboxImagePlaceholder.svg");
        model.addAttribute("message", "Order cancelled.");
        model.addAttribute("link", "/account");
        model.addAttribute("link_name", "Back to the account page");
        return "template_messagebox";
    }
}
