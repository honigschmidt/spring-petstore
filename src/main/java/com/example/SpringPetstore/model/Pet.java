package com.example.SpringPetstore.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pet_generator")
    @SequenceGenerator(name="pet_generator", sequenceName = "pet_seq", allocationSize=50)
    @Column(name = "id", nullable = false)
    private Long id;

    // category

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "status", nullable = false)
    private PetStatus status;

    @OneToOne
    private Order order;
}
