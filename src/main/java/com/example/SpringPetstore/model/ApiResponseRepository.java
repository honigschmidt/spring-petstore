package com.example.SpringPetstore.model;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface ApiResponseRepository extends CrudRepository<ApiResponse, Long> {

    Optional<ApiResponse> findByCodeAndType(Integer code, String type);
}
