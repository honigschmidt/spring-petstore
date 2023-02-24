package com.example.SpringPetstore.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    public static final String IMAGE_PATH_ABS = "src/main/resources/static/images/";
    public static final String IMAGE_PATH_REL = "images/";

    public void store(MultipartFile file) throws Exception {
        Path path = Paths.get(IMAGE_PATH_ABS + file.getOriginalFilename());
        InputStream inputStream = file.getInputStream();
        Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
    }
}
