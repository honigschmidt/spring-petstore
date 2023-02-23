package com.example.SpringPetstore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
//@ToString
@Builder
@Entity
@Table(name = "pets")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pet_generator")
    @SequenceGenerator(name="pet_generator", sequenceName = "pet_seq", allocationSize=50)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @JsonManagedReference
    Category category;

    @Column(name = "name")
    private String name;

    @OneToMany
    @Column(name = "photos")
    @JsonManagedReference
    private Set<Photo> photoSet;

    @ManyToMany
    @JoinTable(
            name = "pet_tags",
            joinColumns = @JoinColumn(name = "pet_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @JsonManagedReference
    private Set<Tag> tagSet;

    @Column(name = "status")
    private PetStatus status;

    @OneToOne(mappedBy = "pet", cascade = CascadeType.ALL)
    @JsonBackReference
    private Order order;

    @Override
    public String toString() {
        return "Pet{" +
                "id=" + id +
                ", category=" + category +
                ", name='" + name + '\'' +
                ", tagSet=" + tagSet +
                ", status=" + status +
                ", order=" + order.getId() +
                '}';
    }
}
