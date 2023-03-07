package com.example.SpringPetstore;

import com.example.SpringPetstore.model.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
public class SpringPetstoreApplication {

    @Autowired
    PetRepository petRepository;
    OrderRepository orderRepository;
    UserRepository userRepository;
    PhotoRepository photoRepository;
    TagRepository tagRepository;
    CategoryRepository categoryRepository;
    ApiResponseRepository apiResponseRepository;

    public SpringPetstoreApplication(PetRepository petRepository, OrderRepository orderRepository, UserRepository userRepository, PhotoRepository photoRepository, TagRepository tagRepository, CategoryRepository categoryRepository, ApiResponseRepository apiResponseRepository) {
        this.petRepository = petRepository;
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.photoRepository = photoRepository;
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

        // Create tags
        tagRepository.save(Tag.builder()
                .name("Tag_1")
                .build());
        tagRepository.save(Tag.builder()
                .name("Tag_2")
                .build());
        tagRepository.save(Tag.builder()
                .name("Tag_3")
                .build());

        // Create categories
        categoryRepository.save(Category.builder()
                .name("Category_1")
                .build());
        categoryRepository.save(Category.builder()
                .name("Category_2")
                .build());
        categoryRepository.save(Category.builder()
                .name("Category_3")
                .build());

        // Create API responses
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

        apiResponseRepository.save(ApiResponse.builder()
                .code(404)
                .type("photo")
                .message("Photo not found")
                .build());

        // Create pets and pet photos
        Pet newPet = new Pet();
        Photo newPhoto = new Photo();

        newPet = petRepository.save(Pet.builder()
                .name("Alpha")
                .description("Eats you alive.")
                .status(PetStatus.AVAILABLE)
                .build());

        newPhoto = photoRepository.save(Photo.builder()
                .url("images/Alpha.jpg")
                .pet(newPet)
                .build());

        newPet = petRepository.save(Pet.builder()
                .name("Beta")
                .description("Returned good. Buyer unavailable.")
                .status(PetStatus.AVAILABLE)
                .build());

        newPhoto = photoRepository.save(Photo.builder()
                .url("images/Beta.jpg")
                .pet(newPet)
                .build());

        newPet = petRepository.save(Pet.builder()
                .name("Charlie")
                .description("He will hear your scream.")
                .status(PetStatus.AVAILABLE)
                .build());

        newPhoto = photoRepository.save(Photo.builder()
                .url("images/Charlie.jpg")
                .pet(newPet)
                .build());

//        // Create orders
//        orderRepository.save(Order.builder()
//                .quantity(0)
//                .pet(newPet)
//                .shipDate(LocalDate.EPOCH)
//                .status(OrderStatus.DUMMY)
//                .complete(Boolean.FALSE)
//                .build());

        // Create users
        userRepository.save(User.builder()
                .username("admin")
                .firstName("John")
                .lastName("Doe")
                .email("admin@nomail.com")
                .password("admin")
                .phone("1234567890")
                .userStatus(1)
                .userRole(UserRole.ADMIN)
                .build());

        userRepository.save(User.builder()
                .username("user")
                .firstName("Jane")
                .lastName("Doe")
                .email("user@nomail.com")
                .password("user")
                .phone("1234567890")
                .userStatus(1)
                .userRole(UserRole.USER)
                .build());
    }
}
