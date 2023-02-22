package com.example.SpringPetstore.controller;

import com.example.SpringPetstore.model.*;
import com.example.SpringPetstore.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
public class PetController {

    @Autowired
    PetService petService;
    TagRepository tagRepository;
    CategoryRepository categoryRepository;
    ApiResponseRepository apiResponseRepository;

    public PetController(PetService petService, TagRepository tagRepository, CategoryRepository categoryRepository, ApiResponseRepository apiResponseRepository) {
        this.petService = petService;
        this.tagRepository = tagRepository;
        this.categoryRepository = categoryRepository;
        this.apiResponseRepository = apiResponseRepository;
    }

    @PostMapping(path = "/pet/form/add")
    @ResponseBody
    public ResponseEntity<Pet> addPet(@RequestParam Long category_id, @RequestParam String name, @RequestParam(value = "tag_id") String[] tag_id_list) {
        Set<Tag> newTagList = new HashSet<>();
        for (String tag_id : tag_id_list) {
            newTagList.add((tagRepository.findById(Long.valueOf(tag_id))).get());
        }
        return ResponseEntity.ok(petService.addPet(Pet.builder()
                .category((categoryRepository.findById(category_id)).get())
                .name(name)
                .tagSet(newTagList)
                .status(PetStatus.AVAILABLE)
                .build()));
    }

    @GetMapping(path = "/pet/form/getbyid")
    @ResponseBody
    public ResponseEntity getPetById(@RequestParam Long pet_id) {
        Optional<Pet> result = petService.getPetById(pet_id);
        if (result.isPresent()) {
            return ResponseEntity.ok(petService.getPetById(pet_id).get());
        } else return ResponseEntity.status(404).body(apiResponseRepository.findByCodeAndType(404, "pet").get());
    }

    @GetMapping(path = "/pet/form/getallhtml")
    public String getAllPetsHTML(Model model) {
        model.addAttribute("pet_list", petService.getAllPets());
        return ("template_pet_list");
    }

    @GetMapping(path = "/pet/form/getalljson")
    public ResponseEntity getAllPetsJSON() {
        return ResponseEntity.ok(petService.getAllPets());
    }

    @GetMapping(path = "/pet/form/update")
    @ResponseBody
    public ResponseEntity updatePetWithForm(@RequestParam Long pet_id, @RequestParam Long category_id, @RequestParam String name, @RequestParam(value = "tag_id") String[] tag_id_list, @RequestParam String pet_status) {
        Pet updatedPet = petService.getPetById(pet_id).get();
        updatedPet.setCategory(categoryRepository.findById(category_id).get());
        updatedPet.setName(name);
        Set<Tag> newTagSet = new HashSet<>();
        for (String tag_id : tag_id_list) {
            newTagSet.add((tagRepository.findById(Long.valueOf(tag_id))).get());
        }
        updatedPet.setTagSet(newTagSet);
        switch (pet_status) {
            case "AVAILABLE":
                updatedPet.setStatus(PetStatus.AVAILABLE);
                break;
            case "PENDING":
                updatedPet.setStatus(PetStatus.PENDING);
                break;
            case "SOLD":
                updatedPet.setStatus(PetStatus.SOLD);
                break;
        }
        return ResponseEntity.ok(petService.updatePetWithForm(pet_id, updatedPet).get());
    }

    @GetMapping(path = "/pet/form/delete")
    @ResponseBody
    public ResponseEntity deletePet(@RequestParam(value = "pet_id") Long[] pet_id_list) {
        for (Long pet_id : pet_id_list) {
            petService.deletePet(pet_id);
        }
        return ResponseEntity.ok().build();
    }
}