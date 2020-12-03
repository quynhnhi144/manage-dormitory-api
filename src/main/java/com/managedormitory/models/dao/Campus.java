package com.managedormitory.models.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Campus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column(length = 100, name = "name", nullable = false)
    @Size(min = 1, max = 50)
    @NonNull
    //@NotBlank(message = "Name is mandatory")
    private String name;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    //@NotBlank(message = "User is mandatory")
    private User userManager;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "campus")
    @JsonIgnore
    private List<Room> rooms;
}
