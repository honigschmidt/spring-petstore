package com.example.SpringPetstore;

import com.example.SpringPetstore.model.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication
public class SpringPetstoreApplication {

    @Autowired
    PetRepository petRepository;
    OrderRepository orderRepository;
    UserRepository userRepository;
    TagRepository tagRepository;
    CategoryRepository categoryRepository;
    ApiResponseRepository apiResponseRepository;

    public SpringPetstoreApplication(PetRepository petRepository, OrderRepository orderRepository, UserRepository userRepository, TagRepository tagRepository, CategoryRepository categoryRepository, ApiResponseRepository apiResponseRepository) {
        this.petRepository = petRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.categoryRepository = categoryRepository;
        this.apiResponseRepository = apiResponseRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(SpringPetstoreApplication.class, args);
    }

    @PostConstruct
    public void init() {
        prepDB();
    }

    public void prepDB() {

        tagRepository.save(Tag.builder()
                .name("Tag_1")
                .build());
        tagRepository.save(Tag.builder()
                .name("Tag_2")
                .build());
        tagRepository.save(Tag.builder()
                .name("Tag_3")
                .build());

        categoryRepository.save(Category.builder()
                .name("Category_1")
                .build());
        categoryRepository.save(Category.builder()
                .name("Category_2")
                .build());
        categoryRepository.save(Category.builder()
                .name("Category_3")
                .build());

        apiResponseRepository.save(ApiResponse.builder()
                .code(404)
                .type("pet")
                .message("Pet not found")
                .build());

        apiResponseRepository.save(ApiResponse.builder()
                .code(404)
                .type("order")
                .message("Order not found")
                .build());

        apiResponseRepository.save(ApiResponse.builder()
                .code(404)
                .type("user")
                .message("User not found")
                .build());

        Pet newPet = petRepository.save(Pet.builder()
                .name("Alpha")
                .photoUrl("images/Alpha.jpg")
                .status(PetStatus.PENDING)
                .build());

        orderRepository.save(Order.builder()
                .quantity(0)
                .pet(newPet)
                .shipDate(LocalDate.EPOCH)
                .status(OrderStatus.DUMMY)
                .complete(Boolean.FALSE)
                .build());

        petRepository.save(Pet.builder()
                .name("Beta")
                .photoUrl("images/Beta.jpg")
                .status(PetStatus.AVAILABLE)
                .build());

        petRepository.save(Pet.builder()
                .name("Charlie")
                .photoUrl("images/Charlie.jpg")
                .status(PetStatus.AVAILABLE)
                .build());

        userRepository.save(User.builder()
                .username("admin")
                .firstName("John")
                .lastName("Doe")
                .email("admin@nomail.com")
                .password("admin")
                .phone("1234567890")
                .userStatus(0)
                .userRole(UserRole.ADMIN)
                .build());

        userRepository.save(User.builder()
                .username("user")
                .firstName("Jane")
                .lastName("Doe")
                .email("user@nomail.com")
                .password("user")
                .phone("1234567890")
                .userStatus(0)
                .userRole(UserRole.USER)
                .build());
    }
}
