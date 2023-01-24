package com.example.SpringPetstore.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends CrudRepository<Pet, Long> {

    Optional<List<Pet>> findByStatus(String status);
}
