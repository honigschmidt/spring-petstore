package com.example.SpringPetstore;

import com.example.SpringPetstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    UserService userService;

    public WebSecurityConfig(UserService userService) {
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .authorizeHttpRequests((requests) -> requests.anyRequest().authenticated())
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll())
                .logout((logout) -> logout
                        .permitAll());
        httpSecurity.csrf().disable();
        return httpSecurity.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("admin")
//                .password("admin")
//                .roles("USER")
//                .build();
//        return new InMemoryUserDetailsManager(user);
//    }

    @Bean
    public UserDetailsService userDetailsService() {
        List<UserDetails> userDetailsList = new ArrayList<>();
        Iterable<com.example.SpringPetstore.model.User> userList = userService.getAllUsers();
        for (com.example.SpringPetstore.model.User user : userList) {
            UserDetails newUser = org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder()
                    .username(user.getUsername())
                    .password(user.getPassword())
                    .roles("USER")
                    .build();
            userDetailsList.add(newUser);
        }
        return new InMemoryUserDetailsManager(userDetailsList);
    }
}