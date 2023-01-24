package com.example.SpringPetstore;

import com.example.SpringPetstore.model.Order;
import com.example.SpringPetstore.model.OrderRepository;
import com.example.SpringPetstore.model.Pet;
import com.example.SpringPetstore.model.PetRepository;
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

	public SpringPetstoreApplication(PetRepository petRepository, OrderRepository orderRepository) {
		this.petRepository = petRepository;
		this.orderRepository = orderRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringPetstoreApplication.class, args);
	}

	@PostConstruct
	public void init() {
		prepDB();
	}

	public void prepDB() {
		orderRepository.save(Order.builder().quantity(0).shipDate(LocalDate.EPOCH).status("NEW").complete(false).build());
		orderRepository.save(Order.builder().quantity(0).shipDate(LocalDate.EPOCH).status("NEW").complete(false).build());
		orderRepository.save(Order.builder().quantity(0).shipDate(LocalDate.EPOCH).status("NEW").complete(false).build());

		petRepository.save(Pet.builder().name("Alex").status("AVAILABLE").build());
		petRepository.save(Pet.builder().name("Benny").status("AVAILABLE").build());
		petRepository.save(Pet.builder().name("Ceasar").status("AVAILABLE").build());

		Order savedOrder = orderRepository.save(Order.builder().quantity(1).shipDate(LocalDate.EPOCH).status("PLACED").complete(false).build());
		petRepository.save(Pet.builder().name("Dalvik").status("PENDING").order(savedOrder).build());
	}
}
