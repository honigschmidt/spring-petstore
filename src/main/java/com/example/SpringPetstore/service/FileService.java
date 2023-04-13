package com.example.SpringPetstore.service;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class FileService {

    public static final String FILE_PATH_LOCAL = "src/main/resources/static/images/";
    public static final String FILE_PATH_SERVER = "images/";

    public void savePhoto(MultipartFile file, String photoUID) throws Exception {
        InputStream inputStream = file.getInputStream();
        Path path = Paths.get(FILE_PATH_LOCAL + photoUID + file.getOriginalFilename());
        Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
    }

    public void deletePhoto(String url) throws Exception {
        url = url.replace(FILE_PATH_SERVER, FILE_PATH_LOCAL);
        Path path = Paths.get(url);
        Files.delete(path);
    }

    public String generatePhotoUID() {
        return (RandomStringUtils.random(8, "0123456789abcdef") + "_");
    }
}
