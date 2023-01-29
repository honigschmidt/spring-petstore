package com.example.SpringPetstore.view;

import com.example.SpringPetstore.model.Pet;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class JsonPet {

    private Long id;
    private String name;
    private String status;
    private Long order_id;

    public JsonPet(Pet pet) {
        this.id = pet.getId();
        this.name = pet.getName();
        this.status = pet.getStatus().toString();
        if (pet.getOrder() != null) {
            this.order_id = pet.getOrder().getId();
        }
    }
}
