package com.example.SpringPetstore.service;

import com.example.SpringPetstore.model.Photo;
import com.example.SpringPetstore.model.PhotoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PhotoService {

    @Autowired
    PhotoRepository photoRepository;

    public PhotoService(PhotoRepository photoRepository) {
        this.photoRepository = photoRepository;
    }

    public Photo addPhoto(Photo photo) {
        return photoRepository.save(photo);
    }

    public Optional<Photo> getPhotoById(Long id) {
        return photoRepository.findById(id);
    }

    public Iterable<Photo> getAllPhotos() {
        return photoRepository.findAll();
    }

    public void deletePhoto(Long id) {
        photoRepository.deleteById(id);
    }
}
