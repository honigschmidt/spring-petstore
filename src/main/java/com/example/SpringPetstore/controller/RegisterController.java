package com.example.SpringPetstore.controller;

import com.example.SpringPetstore.model.User;
import com.example.SpringPetstore.model.UserRole;
import com.example.SpringPetstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RegisterController {

    @Autowired
    private UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/register")
    @ResponseBody
    public String registerUser(@RequestParam String user_name, @RequestParam String password) {
        userService.addUser(User.builder()
                .username(user_name)
                .password(password)
                .userRole(UserRole.USER)
                .build());
        return "OK";
    }
}
