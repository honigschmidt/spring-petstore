package com.example.SpringPetstore.controller;

import com.example.SpringPetstore.model.User;
import com.example.SpringPetstore.model.UserRole;
import com.example.SpringPetstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    // TODO Finalize the /register controller
    @PostMapping(path = "/register")
    @ResponseBody
    public String registerUser(@RequestParam String user_name, @RequestParam String password) {
        // Add to DB
        userService.addUser(User.builder()
                .username(user_name)
                .password(password)
                .userRole(UserRole.USER)
                .build());
        // Add to memory
        inMemoryUserDetailsManager.createUser(org.springframework.security.core.userdetails.User.builder()
                .username(user_name)
                .password(bCryptPasswordEncoder.encode(password))
                .roles(UserRole.USER.toString())
                .build());

//        List<GrantedAuthority> grantedAuthorityList = Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
//
//        Authentication authentication = new UsernamePasswordAuthenticationToken(user_name, null, grantedAuthorityList);
//        SecurityContextHolder.getContext().setAuthentication(authentication);

        return (inMemoryUserDetailsManager.loadUserByUsername(user_name)).toString();
    }
}
