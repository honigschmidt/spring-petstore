package com.example.SpringPetstore.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_generator")
    @SequenceGenerator(name="order_generator", sequenceName = "order_seq", allocationSize=50)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToMany(mappedBy = "order")
    private List<Pet> petList = new ArrayList<>();

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "ship_date", nullable = false)
    private LocalDate shipDate;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "complete", nullable = false)
    private Boolean complete;
}
