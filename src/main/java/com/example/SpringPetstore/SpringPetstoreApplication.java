package com.example.SpringPetstore;

import com.example.SpringPetstore.model.*;
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
	TagRepository tagRepository;
	CategoryRepository categoryRepository;

	public SpringPetstoreApplication(PetRepository petRepository, OrderRepository orderRepository, TagRepository tagRepository, CategoryRepository categoryRepository) {
		this.petRepository = petRepository;
		this.orderRepository = orderRepository;
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
	}
}
