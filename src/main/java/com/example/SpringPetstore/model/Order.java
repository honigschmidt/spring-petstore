package com.example.SpringPetstore.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "order_table")
public class Order {

//    enum Status {
//        DUMMY,
//        PLACED,
//        APPROVED,
//        DELIVERED
//    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToMany(mappedBy = "order")
    private Set<Pet> pets;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "ship_date", nullable = false)
    private LocalDate shipDate;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "complete", nullable = false)
    private Boolean complete;
}
