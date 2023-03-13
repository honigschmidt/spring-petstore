package com.example.SpringPetstore.controller.entity;

import com.example.SpringPetstore.model.ApiResponseRepository;
import com.example.SpringPetstore.model.Photo;
import com.example.SpringPetstore.service.FileService;
import com.example.SpringPetstore.service.PetService;
import com.example.SpringPetstore.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
public class PhotoController {

    @Autowired
    PhotoService photoService;
    FileService fileService;
    PetService petService;
    ApiResponseRepository apiResponseRepository;

    public PhotoController(PhotoService photoService, FileService fileService, PetService petService, ApiResponseRepository apiResponseRepository) {
        this.photoService = photoService;
        this.fileService = fileService;
        this.petService = petService;
        this.apiResponseRepository = apiResponseRepository;
    }

    @PostMapping(path = "/photo/form/add")
    @ResponseBody
    public ResponseEntity<Photo> addPhoto(@RequestParam Long pet_id, @RequestParam String photo_metadata, @RequestParam MultipartFile file) {
        try {
            String photoUID = fileService.generatePhotoUID();
            fileService.storePetPhoto(file, photoUID);
            Photo newPhoto = photoService.addPhoto(Photo.builder()
                    .metaData(photo_metadata)
                    .url(FileService.IMAGE_PATH_SERVER + photoUID + file.getOriginalFilename())
                    .pet(petService.getPetById(pet_id).get())
                    .build());
            return ResponseEntity.ok(newPhoto);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    @GetMapping(path = "/photo/form/getbyid")
    public ResponseEntity getPhotoById(@RequestParam Long id) {
        Optional<Photo> result = photoService.getPhotoById(id);
        if (result.isPresent()) {
            return ResponseEntity.ok(result.get());
        } else return ResponseEntity.status(404).body(apiResponseRepository.findByCodeAndType(404, "photo").get());
    }

    @GetMapping(path = "/photo/form/getallhtml")
    public String getAllPhotosHTML(Model model) {
        model.addAttribute("photo_list", photoService.getAllPhotos());
        return "template_photo_list";
    }

    @GetMapping(path = "/photo/form/getalljson")
    @ResponseBody
    public ResponseEntity<Iterable<Photo>> getAllPhotos() {
        return ResponseEntity.ok(photoService.getAllPhotos());
    }

    @GetMapping(path = "/photo/form/delete")
    @ResponseBody
    public ResponseEntity deletePhotoById(@RequestParam(value = "photo_id") Long[] photo_id_list) {
        String photoUrl = null;
        for (Long photo_id : photo_id_list) {
            photoUrl = photoService.getPhotoById(photo_id).get().getUrl();
            photoService.deletePhoto(photo_id);
            try {
                fileService.deletePhoto(photoUrl);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return ResponseEntity.ok().build();
    }
}
