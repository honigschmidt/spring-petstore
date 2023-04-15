package com.example.SpringPetstore.controller.entity;

import com.example.SpringPetstore.model.ApiResponseRepository;
import com.example.SpringPetstore.model.User;
import com.example.SpringPetstore.model.UserRole;
import com.example.SpringPetstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Optional;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    private ApiResponseRepository apiResponseRepository;
    private InMemoryUserDetailsManager inMemoryUserDetailsManager;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserController(UserService userService, ApiResponseRepository apiResponseRepository, InMemoryUserDetailsManager inMemoryUserDetailsManager, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userService = userService;
        this.apiResponseRepository = apiResponseRepository;
        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostMapping(path = "/user/form/add")
    @ResponseBody
    public ResponseEntity addUser(@RequestParam String user_name, @RequestParam String first_name, @RequestParam String last_name, @RequestParam String email, @RequestParam String password, @RequestParam String phone) {
        if (userService.getUserByUsername(user_name).isEmpty()) {
            inMemoryUserDetailsManager.createUser(org.springframework.security.core.userdetails.User.builder()
                    .username(user_name)
                    .password(bCryptPasswordEncoder.encode(password))
                    .roles(UserRole.USER.toString())
                    .build());
            return ResponseEntity
                    .ok(userService.addUser(User.builder()
                            .username(user_name)
                            .firstName(first_name)
                            .lastName(last_name)
                            .email(email)
                            .password(bCryptPasswordEncoder.encode(password))
                            .phone(phone)
                            .userRole(UserRole.USER)
                            .build()));
        } else return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("User already exists.");
    }

    @GetMapping(path = "/user/form/getbyid")
    public ResponseEntity getUserById(@RequestParam Long user_id) {
        Optional<User> result = userService.getUserById(user_id);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else return ResponseEntity.status(404).body(apiResponseRepository.findByCodeAndType(404, "user").get());
    }

    @GetMapping(path = "/user/form/getallhtml")
    public String getAllUsersHTML(Model model) {
        model.addAttribute("allUsers", userService.getAllUsers());
        return "template_user_list";
    }

    @GetMapping(path = "/user/form/getalljson")
    @ResponseBody
    public ResponseEntity<Iterable<User>> getAllUsersJSON() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping(path = "/user/form/update")
    public ResponseEntity<User> updateUser(@CurrentSecurityContext(expression = "authentication?.name") String currentUser, @RequestParam Long user_id, @RequestParam String user_name, @RequestParam String first_name, @RequestParam String last_name, @RequestParam String email, @RequestParam String password, @RequestParam String phone) {
        User updatedUser = userService.getUserById(user_id).get();
        updatedUser.setUsername(user_name);
        updatedUser.setFirstName(first_name);
        updatedUser.setLastName(last_name);
        updatedUser.setEmail(email);
        updatedUser.setPassword(bCryptPasswordEncoder.encode(password));
        updatedUser.setPhone(phone);
        inMemoryUserDetailsManager.updatePassword(inMemoryUserDetailsManager.loadUserByUsername(currentUser), bCryptPasswordEncoder.encode(password));
        return ResponseEntity.ok(userService.updateUserWithForm(updatedUser));
    }

    @PostMapping(path = "/user/form/delete")
    @ResponseBody
    public ResponseEntity deleteUser(@RequestParam(value = "user_id") Long[] user_id_list) {
        for (Long user_id : user_id_list) {
            userService.deleteUser(user_id);
        }
        return ResponseEntity.ok().build();
    }
}
