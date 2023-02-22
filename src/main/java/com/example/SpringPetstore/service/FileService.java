package com.example.SpringPetstore.service;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    public void storeFS(MultipartFile file) {
        // TODO: Get path from application.properties
        Path path = Paths.get("upload");
        try {
            Files.createDirectories(path);
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream, path.resolve(StringUtils.cleanPath(file.getOriginalFilename())), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void storeDB() {
        // TODO: Store file in database
    }
}
