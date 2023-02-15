package com.example.SpringPetstore.service;

import com.example.SpringPetstore.model.Pet;
import com.example.SpringPetstore.model.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService {

    @Autowired
    PetRepository petRepository;

    public PetService(PetRepository petRepository) {
        this.petRepository = petRepository;
    }

    public Pet addPet(Pet pet) {
        return petRepository.save(pet);
    }

    public Optional<Pet> getPetById(Long id) {
        return petRepository.findById(id);
    }

    public Iterable<Pet> getAllPets() {
        return petRepository.findAll();
    }

    public Optional<Pet> updatePetWithForm(Long id, Pet pet) {
        return Optional.of(petRepository.save(pet));
    }

    public void deletePet(Long id) {
        petRepository.deleteById(id);
    }
}
