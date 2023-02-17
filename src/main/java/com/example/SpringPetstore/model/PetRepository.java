package com.example.SpringPetstore.model;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PetRepository extends CrudRepository<Pet, Long> {

    Optional<Iterable<Pet>> findByStatus (PetStatus petStatus);
}
