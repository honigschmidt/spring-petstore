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
@Table(name = "apiresponses")
public class ApiResponse {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "apiresponse_generator")
    @SequenceGenerator(name="apiresponse_generator", sequenceName = "apiresponse_seq", allocationSize=50)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "type")
    private String type;

    @Column(name = "message")
    private String message;
}
