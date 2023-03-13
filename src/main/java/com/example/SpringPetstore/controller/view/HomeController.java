package com.example.SpringPetstore.controller.view;

import com.example.SpringPetstore.model.Pet;
import com.example.SpringPetstore.model.PetStatus;
import com.example.SpringPetstore.model.Photo;
import com.example.SpringPetstore.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HomeController {

    @Autowired
    PetService petService;

    public HomeController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping(path = "/")
    public String getHome(Model model) {
        Iterable<Pet> availablePets = petService.getPetsByStatus(PetStatus.AVAILABLE).get();
        Map<String, String> availablePetsPhotoMap = new HashMap<>();
        List<String> petPhotoList = new ArrayList<>();
        for (Pet pet : availablePets) {
            for (Photo photo : pet.getPhotoSet()) {
                petPhotoList.add(photo.getUrl());
            }
            if (!petPhotoList.isEmpty())
                availablePetsPhotoMap.put(pet.getName(), petPhotoList.get(0));
            petPhotoList.clear();
        }
        model.addAttribute("available_pet_list", availablePets);
        model.addAttribute("pet_photo_list", availablePetsPhotoMap);
        return "template_home";
    }
}
