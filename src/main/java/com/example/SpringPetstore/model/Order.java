package com.example.SpringPetstore.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//@ToString
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "order_generator")
    @SequenceGenerator(name="order_generator", sequenceName = "order_seq", allocationSize=50)
    @Column(name = "id", nullable = false)
    private Long id;

    // TODO: Check cascading
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "pet_id", referencedColumnName = "id")
    @JsonManagedReference
    private Pet pet;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "ship_date")
    private LocalDate shipDate;

    @Column(name = "status")
    private OrderStatus status;

    @Column(name = "complete")
    private Boolean complete;

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", pet=" + pet.getId() +
                ", quantity=" + quantity +
                ", shipDate=" + shipDate +
                ", status=" + status +
                ", complete=" + complete +
                '}';
    }
}
