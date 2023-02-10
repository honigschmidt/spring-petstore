package com.example.SpringPetstore.service;

import com.example.SpringPetstore.model.User;
import com.example.SpringPetstore.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User addUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> updateUserWithForm(Long id, User user) {
        Optional<User> searchResult = userRepository.findById(id);
        if (searchResult.isPresent()) {
            User updatedUser = searchResult.get();
            updatedUser.setUsername(user.getUsername());
            updatedUser.setFirstName(user.getFirstName());
            updatedUser.setLastName(user.getLastName());
            updatedUser.setEmail(user.getEmail());
            updatedUser.setPassword(user.getPassword());
            updatedUser.setPhone(user.getPhone());
            return Optional.of(userRepository.save(updatedUser));
        } else return Optional.empty();
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
