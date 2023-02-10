package com.example.SpringPetstore;

import com.example.SpringPetstore.model.*;
import com.example.SpringPetstore.service.OrderService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SpringPetstoreApplication {

	@Autowired
	PetRepository petRepository;
	OrderRepository orderRepository;
	UserRepository userRepository;
	TagRepository tagRepository;
	CategoryRepository categoryRepository;

	public SpringPetstoreApplication(PetRepository petRepository, OrderRepository orderRepository, UserRepository userRepository, TagRepository tagRepository, CategoryRepository categoryRepository) {
		this.petRepository = petRepository;
		this.orderRepository = orderRepository;
		this.userRepository = userRepository;
		this.tagRepository = tagRepository;
		this.categoryRepository = categoryRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringPetstoreApplication.class, args);
	}

	@PostConstruct
	public void init() {
		prepDB();
	}

	public void prepDB() {

		tagRepository.save(Tag.builder().name("Tag_1").build());
		tagRepository.save(Tag.builder().name("Tag_2").build());
		tagRepository.save(Tag.builder().name("Tag_3").build());

		categoryRepository.save(Category.builder().name("Category_1").build());
		categoryRepository.save(Category.builder().name("Category_2").build());
		categoryRepository.save(Category.builder().name("Category_3").build());

		Pet newPet = petRepository.save(Pet.builder().
				name("Alex").
				status(PetStatus.PENDING).
				build());

		Order newOrder = orderRepository.save(Order.builder().
				quantity(0).
				pet(newPet).
				shipDate(LocalDate.EPOCH).
				status(OrderStatus.DUMMY).
				complete(Boolean.FALSE).
				build());

		petRepository.save(Pet.builder().
				name("Billy").
				status(PetStatus.AVAILABLE).
				build());

		userRepository.save(User.builder().
				username("johndoe").
				firstName("John").
				lastName("Doe").
				email("john.doe@no-mail.com").
				password("start1234").
				phone("1234567890").
				userStatus(0).
				build());
	}
}
