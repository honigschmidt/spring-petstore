package com.example.SpringPetstore.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_generator")
    @SequenceGenerator(name="order_generator", sequenceName = "order_seq", allocationSize=50)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne
    @JoinColumn(name = "pet_id", referencedColumnName = "id", nullable = false)
    private Pet pet;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "ship_date", nullable = false)
    private LocalDate shipDate;

    @Column(name = "status", nullable = false)
    private OrderStatus status;

    @Column(name = "complete", nullable = false)
    private Boolean complete;
}
