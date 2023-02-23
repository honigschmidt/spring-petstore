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

    private static final String IMAGE_PATH = "src/main/resources/static/images/";

    public void store(MultipartFile file) throws IOException {
        Path path = Paths.get(IMAGE_PATH + file.getOriginalFilename());
        InputStream inputStream = file.getInputStream();
        Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
    }
}
