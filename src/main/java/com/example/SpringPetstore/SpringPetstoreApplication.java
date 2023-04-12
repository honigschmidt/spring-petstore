package com.example.SpringPetstore;

import com.example.SpringPetstore.model.*;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

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

    String loremIpsum = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut in sodales eros. Sed pharetra tincidunt felis, quis gravida massa vestibulum vel.";

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

        apiResponseRepository.save(ApiResponse.builder()
                .code(404)
                .type("photo")
                .message("Photo not found")
                .build());

        Pet newPet = new Pet();
        Photo newPhoto = new Photo();

        newPet = petRepository.save(Pet.builder()
                .name("Alpha")
                .description(loremIpsum)
                .status(PetStatus.AVAILABLE)
                .build());

        newPhoto = photoRepository.save(Photo.builder()
                .url("images/Alpha.svg")
                .pet(newPet)
                .build());

        newPet = petRepository.save(Pet.builder()
                .name("Beta")
                .description(loremIpsum)
                .status(PetStatus.AVAILABLE)
                .build());

        newPhoto = photoRepository.save(Photo.builder()
                .url("images/Beta.svg")
                .pet(newPet)
                .build());

        newPet = petRepository.save(Pet.builder()
                .name("Charlie")
                .description(loremIpsum)
                .status(PetStatus.AVAILABLE)
                .build());

        newPhoto = photoRepository.save(Photo.builder()
                .url("images/Charlie.svg")
                .pet(newPet)
                .build());

        newPet = petRepository.save(Pet.builder()
                .name("Delta")
                .description(loremIpsum)
                .status(PetStatus.AVAILABLE)
                .build());

        newPhoto = photoRepository.save(Photo.builder()
                .url("images/Delta.svg")
                .pet(newPet)
                .build());

        newPet = petRepository.save(Pet.builder()
                .name("Echo")
                .description(loremIpsum)
                .status(PetStatus.AVAILABLE)
                .build());

        newPhoto = photoRepository.save(Photo.builder()
                .url("images/Echo.svg")
                .pet(newPet)
                .build());

        newPet = petRepository.save(Pet.builder()
                .name("Foxtrot")
                .description(loremIpsum)
                .status(PetStatus.AVAILABLE)
                .build());

        newPhoto = photoRepository.save(Photo.builder()
                .url("images/Foxtrot.svg")
                .pet(newPet)
                .build());

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
