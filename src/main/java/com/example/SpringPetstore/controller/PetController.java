package com.example.SpringPetstore.controller;

import com.example.SpringPetstore.model.*;
import com.example.SpringPetstore.service.FileService;
import com.example.SpringPetstore.service.PetService;
import com.example.SpringPetstore.service.PhotoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Controller
public class PetController {

    @Autowired
    PetService petService;
    PhotoService photoService;
    FileService fileService;
    TagRepository tagRepository;
    CategoryRepository categoryRepository;
    ApiResponseRepository apiResponseRepository;

    Logger logger = LoggerFactory.getLogger(PetController.class);

    public PetController(PetService petService, PhotoService photoService, FileService fileService, TagRepository tagRepository, CategoryRepository categoryRepository, ApiResponseRepository apiResponseRepository) {
        this.petService = petService;
        this.photoService = photoService;
        this.fileService = fileService;
        this.tagRepository = tagRepository;
        this.categoryRepository = categoryRepository;
        this.apiResponseRepository = apiResponseRepository;
    }

    @PostMapping(path = "/pet/form/add")
    @ResponseBody
    public ResponseEntity<Pet> addPet(@RequestParam Long category_id, @RequestParam String name, @RequestParam MultipartFile file, @RequestParam String photo_metadata, @RequestParam(value = "tag_id") String[] tag_id_list) {
        Set<Tag> newTagList = new HashSet<>();
        for (String tag_id : tag_id_list) {
            newTagList.add((tagRepository.findById(Long.valueOf(tag_id))).get());
        }
        Pet newPet = petService.addPet(Pet.builder()
                .category((categoryRepository.findById(category_id)).get())
                .name(name)
                .tagSet(newTagList)
                .status(PetStatus.AVAILABLE)
                .build());
        if (file.getSize() != 0) {
            try {
                String photoUID = fileService.generatePhotoUID();
                fileService.storePetPhoto(file, photoUID);
                Photo newPhoto = photoService.addPhoto(Photo.builder()
                        .metaData(photo_metadata)
                        .url(FileService.IMAGE_PATH_RELATIVE + photoUID + file.getOriginalFilename())
                        .pet(newPet)
                        .build());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return ResponseEntity.ok(newPet);
    }

    @GetMapping(path = "/pet/form/getbyid")
    public ResponseEntity getPetById(@RequestParam Long pet_id) {
        Optional<Pet> result = petService.getPetById(pet_id);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else return ResponseEntity.status(404).body(apiResponseRepository.findByCodeAndType(404, "pet").get());
    }

    @GetMapping(path = "/pet/form/getallhtml")
    public String getAllPetsHTML(Model model) {
        model.addAttribute("pet_list", petService.getAllPets());

        Map<String, List<String>> petPhotoList = new HashMap<>();
        Iterable<Pet> petList = petService.getAllPets();
        for (Pet pet : petList) {
            List<String> photoUrlList = new ArrayList<>();
            for (Photo photo : pet.getPhotoSet()) {
                photoUrlList.add(photo.getUrl());
            }
            petPhotoList.put(pet.getName(), photoUrlList);
        }
        System.out.println(petPhotoList);
        model.addAttribute("pet_photo_list", petPhotoList);

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
        Pet deletedPet = new Pet();
        for (Long pet_id : pet_id_list) {
            deletedPet = petService.getPetById(pet_id).get();
            petService.deletePet(pet_id);
            for (Photo photo : deletedPet.getPhotoSet()) {
                try {
                    fileService.deletePhoto(photo.getUrl());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return ResponseEntity.ok().build();
    }
}
