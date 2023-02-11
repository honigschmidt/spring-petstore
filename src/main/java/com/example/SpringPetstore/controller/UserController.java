package com.example.SpringPetstore.controller;

import com.example.SpringPetstore.model.User;
import com.example.SpringPetstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
    UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping(path = "/user/form/add")
    @ResponseBody
    public ResponseEntity<User> addUser(@RequestParam String user_name, @RequestParam String first_name, @RequestParam String last_name, @RequestParam String email, @RequestParam String password, @RequestParam String phone) {
        return ResponseEntity.ok(userService.addUser(User.builder().
                username(user_name).
                firstName(first_name).
                lastName(last_name).
                email(email).
                password(password).
                phone(phone).
                build()));
    }

    @GetMapping(path = "/user/form/getbyid")
    @ResponseBody
    public ResponseEntity<User> getUserById(@RequestParam Long user_id) {
        Optional<User> result = userService.getUserById(user_id);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else return ResponseEntity.notFound().build();
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

    @GetMapping(path = "/user/form/update")
    public ResponseEntity<User> updateUser(@RequestParam Long user_id, @RequestParam String user_name, @RequestParam String first_name, @RequestParam String last_name, @RequestParam String email, @RequestParam String password, @RequestParam String phone) {
        Optional<User> result = userService.getUserById(user_id);
        if (result.isPresent()) {
            User updatedUser = result.get();
            updatedUser.setUsername(user_name);
            updatedUser.setFirstName(first_name);
            updatedUser.setLastName(last_name);
            updatedUser.setEmail(email);
            updatedUser.setPassword(password);
            updatedUser.setPhone(phone);
            return ResponseEntity.ok(userService.updateUserWithForm(user_id, updatedUser).get());
        } else return ResponseEntity.notFound().build();
    }

    @GetMapping(path = "/user/form/delete")
    @ResponseBody
    public ResponseEntity<User> deleteUser(@RequestParam(value = "user_id") Long[] user_id_list) {
        for (Long user_id : user_id_list) {
            userService.deleteUser(user_id);
        }
        return ResponseEntity.ok().build();
    }
}
