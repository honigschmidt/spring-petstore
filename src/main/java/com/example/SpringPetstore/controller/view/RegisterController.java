package com.example.SpringPetstore.controller.view;

import com.example.SpringPetstore.model.User;
import com.example.SpringPetstore.model.UserRole;
import com.example.SpringPetstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public RegisterController(UserService userService, InMemoryUserDetailsManager inMemoryUserDetailsManager, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping(path = "/register")
    public String getView(Model model) {
        return "template_register";
    }

    @PostMapping(path = "/register")
    public String registerUser(@RequestParam String user_name, @RequestParam String password, @RequestParam String first_name, @RequestParam String last_name, @RequestParam String email, @RequestParam String phone, Model model) {
        if (userService.getUserByUsername(user_name).isEmpty()) {
            userService.addUser(User.builder()
                    .username(user_name)
                    .password(bCryptPasswordEncoder.encode(password))
                    .userRole(UserRole.USER)
                    .build());
            inMemoryUserDetailsManager.createUser(org.springframework.security.core.userdetails.User.builder()
                    .username(user_name)
                    .password(bCryptPasswordEncoder.encode(password))
                    .roles(UserRole.USER.toString())
                    .build());
            model.addAttribute("image", "~/images/MessageboxImageHappy.svg");
            model.addAttribute("message", "Welcome " + user_name + ", thank you for your registration!");
            model.addAttribute("link", "/");
            model.addAttribute("link_name", "Back to the welcome page");
            return "template_messagebox";
        } else {
            model.addAttribute("image", "~/images/MessageboxImageNeutral.svg");
            model.addAttribute("message", "Username " + user_name + " already exist, please choose a different username.");
            model.addAttribute("link", "/register");
            model.addAttribute("link_name", "Back to the registration");
            return "template_messagebox";
        }
    }
}
