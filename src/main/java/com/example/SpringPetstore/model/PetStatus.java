package com.example.SpringPetstore.model;

import java.util.ArrayList;
import java.util.List;

public enum PetStatus {
    AVAILABLE,
    PENDING,
    SOLD;

    public static List<String> getPetStatusList() {
        List<String> petStatusList = new ArrayList<>();
        petStatusList.add((PetStatus.AVAILABLE).toString());
        petStatusList.add((PetStatus.PENDING).toString());
        petStatusList.add((PetStatus.SOLD).toString());
        return petStatusList;
    }
}
