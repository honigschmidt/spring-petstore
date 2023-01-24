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

    public Optional<List<Pet>> findPetsByStatus(String status) {
        return petRepository.findByStatus(status);
    }

    public Optional<Pet> updatePetWithForm(Long id, Pet pet) {
        Optional<Pet> searchResult = petRepository.findById(id);
        if (searchResult.isPresent()) {
            Pet updatedPet = searchResult.get();
            updatedPet.setName(pet.getName());
            updatedPet.setStatus(pet.getStatus());
//            updatedPet.setOrder(pet.getOrder());
            return Optional.of(petRepository.save(updatedPet));
        } else return Optional.empty();
    }

    public String deletePet(Long id) {
        Optional<Pet> searchResult = petRepository.findById(id);
        if (searchResult.isPresent()) {
            petRepository.deleteById(id);
            return ("Pet deleted.");
        } else return ("Pet ID not found.");
    }
}
