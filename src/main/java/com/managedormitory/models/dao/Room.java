package com.managedormitory.models.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 20)
    private Integer id;

    @Column(length = 100, name = "name", nullable = false)
    @Size(min = 1, max = 50)
    @NonNull
    private String name;

    @Column(length = 3)
    private Integer quantityStudent;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "type_room_id")
    @NonNull
    private TypeRoom typeRoom;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "campus_id")
    @NonNull
    private Campus campus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "price_list_id")
    @NonNull
    private PriceList priceList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
    @JsonIgnore
    private List<Student> students;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
    @JsonIgnore
    private List<PowerBill> powerBills;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
    @JsonIgnore
    private List<RegisterRoom> registerRooms;
}
