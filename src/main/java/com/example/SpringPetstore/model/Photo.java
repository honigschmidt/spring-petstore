package com.example.SpringPetstore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name = "photos")
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "photo_generator")
    @SequenceGenerator(name="photo_generator", sequenceName = "photo_seq", allocationSize=50)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "metadata")
    private String metaData;

    @Column(name = "url")
    private String url;

    @Lob
    @Column(name = "file")
    private byte[] file;

    @ManyToOne
    @JoinColumn(name = "pet_id", referencedColumnName = "id")
    @JsonBackReference
    private Pet pet;
}
