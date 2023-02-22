package com.example.SpringPetstore.controller;

import com.example.SpringPetstore.model.Photo;
import com.example.SpringPetstore.service.FileService;
import com.example.SpringPetstore.service.PhotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class PhotoController {

    @Autowired
    PhotoService photoService;
    FileService fileService;

    public PhotoController(PhotoService photoService, FileService fileService) {
        this.photoService = photoService;
        this.fileService = fileService;
    }

    @PostMapping(path = "/photo/form/add")
    @ResponseBody
    public ResponseEntity<Photo> addPhoto(@RequestParam MultipartFile file) {
        fileService.storeFS(file);
        return ResponseEntity.ok().build();
    }
//    public ResponseEntity<Photo> addPhoto(@RequestParam String metadata, @RequestParam MultipartFile file) {
//        return null;
//    }

//    @GetMapping(path = "/photo/form/getbyid")

//    @GetMapping(path = "/photo/form/getallhtml")

//    @GetMapping(path = "/photo/form/getalljson")
//    public ResponseEntity<Iterable<Photo>> getAllPhotos() {
//        return ResponseEntity.ok(photoService.getAllPhotos());
//    }

//    @GetMapping(path = "/photo/form/delete")
}
