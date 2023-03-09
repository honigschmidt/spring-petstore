package com.example.SpringPetstore.controller;

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
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String getRegisterView(Model model) {
        return "template_register";
    }

    @PostMapping(path = "/register")
    @ResponseBody
    public String registerUser(@RequestParam String user_name, @RequestParam String password, @RequestParam String first_name, @RequestParam String last_name, @RequestParam String email, @RequestParam String phone) {

        userService.addUser(User.builder()
                .username(user_name)
                .password(password)
                .userRole(UserRole.USER)
                .build());

        inMemoryUserDetailsManager.createUser(org.springframework.security.core.userdetails.User.builder()
                .username(user_name)
                .password(bCryptPasswordEncoder.encode(password))
                .roles(UserRole.USER.toString())
                .build());

        return ("Hello " + user_name + ", thank you for your registration. Please <a href=\"/store\">log in</a> to enter the store.");
    }
}
