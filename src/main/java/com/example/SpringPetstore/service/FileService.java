package com.example.SpringPetstore.service;

import com.example.SpringPetstore.model.Pet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    @Autowired
    PetService petService;

    public FileService(PetService petService) {
        this.petService = petService;
    }

    public static final String IMAGE_PATH_ABSOLUTE = "src/main/resources/static/images/";
    public static final String IMAGE_PATH_RELATIVE = "images/";

    public void storePetPhoto(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        Path path = Paths.get(IMAGE_PATH_ABSOLUTE + file.getOriginalFilename());
        Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
    }

    public void deletePhoto(String url) throws Exception {
        url = url.replace(IMAGE_PATH_RELATIVE, IMAGE_PATH_ABSOLUTE);
        Path path = Paths.get(url);
        Files.delete(path);
    }
}
