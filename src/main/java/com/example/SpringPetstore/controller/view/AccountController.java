package com.example.SpringPetstore.controller.view;

import com.example.SpringPetstore.model.Order;
import com.example.SpringPetstore.model.Pet;
import com.example.SpringPetstore.model.PetStatus;
import com.example.SpringPetstore.model.User;
import com.example.SpringPetstore.service.OrderService;
import com.example.SpringPetstore.service.PetService;
import com.example.SpringPetstore.service.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccountController {

    @Autowired
    private OrderService orderService;
    private UserService userService;
    private PetService petService;
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;
    private BCryptPasswordEncoder bCryptPasswordEncoder;


    public AccountController(OrderService orderService, UserService userService, PetService petService, InMemoryUserDetailsManager inMemoryUserDetailsManager, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.orderService = orderService;
        this.userService = userService;
        this.petService = petService;
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping(path = "/account")
    public String getView(@CurrentSecurityContext(expression = "authentication?.name") String currentUser, Model model) {
        model.addAttribute("user", userService.getUserByUsername(currentUser).get());
        model.addAttribute("orders", orderService.getOrdersByUserId(userService.getUserByUsername(currentUser).get().getId()).get());
        return "template_account";
    }

    @PostMapping(path = "/account/password/change")
    public String changePassword(@CurrentSecurityContext(expression = "authentication?.name") String currentUser, @RequestParam String old_password, @RequestParam String new_password, Model model) {
        if (bCryptPasswordEncoder.matches(old_password, userService.getUserByUsername(currentUser).get().getPassword())) {
            User updatedUserDb = userService.getUserByUsername(currentUser).get();
            updatedUserDb.setPassword(bCryptPasswordEncoder.encode(new_password));
            userService.updateUserWithForm(updatedUserDb);
            inMemoryUserDetailsManager.updatePassword(inMemoryUserDetailsManager.loadUserByUsername(currentUser), bCryptPasswordEncoder.encode(new_password));
            model.addAttribute("image", "~/images/MessageboxImagePlaceholder.svg");
            model.addAttribute("message", "Password change successful.");
            model.addAttribute("link", "/account");
            model.addAttribute("link_name", "Back to the account page");
            return "template_messagebox";
        } else {
            model.addAttribute("image", "~/images/MessageboxImagePlaceholder.svg");
            model.addAttribute("message", "Old password does not match, current password unchanged.");
            model.addAttribute("link", "/account");
            model.addAttribute("link_name", "Back to the account page");
            return "template_messagebox";
        }
    }

    @GetMapping(path = "/account/user/delete")
    public String getDeleteUserConfirmView(Model model) {
        model.addAttribute("image", "~/images/DialogboxImageSad.svg");
        model.addAttribute("message", "Your account will be deleted. Are you sure?");
        model.addAttribute("action", "/account/user/delete");
        model.addAttribute("method", "post");
        // TODO: Make a CANCEL here
        model.addAttribute("button", "DELETE");
        return "template_dialogbox";
    }

    @PostMapping(path = "/account/user/delete")
    public String deleteUser(@CurrentSecurityContext(expression = "authentication?.name") String currentUser, HttpServletRequest request, Model model) throws ServletException {
        Iterable<Order> orderList = orderService.getOrdersByUserId(userService.getUserByUsername(currentUser).get().getId()).get();
        for (Order order: orderList) {
            Pet updatedPet = order.getPet();
            updatedPet.setStatus(PetStatus.AVAILABLE);
            petService.updatePetWithForm(updatedPet);
        }
        userService.deleteUser(userService.getUserByUsername(currentUser).get().getId());
        inMemoryUserDetailsManager.deleteUser(currentUser);
        request.logout();
        return "redirect:/";
    }

    @PostMapping(path = "/account/order/delete")
    public String deleteOrder(@CurrentSecurityContext(expression = "authentication?.name") String currentUser, @RequestParam Long order_id, @RequestParam Long pet_id, Model model) {
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
