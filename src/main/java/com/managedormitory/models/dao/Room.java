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
    @Column
    private Integer id;

    @Column(length = 100, name = "name", nullable = false)
    @Size(min = 1, max = 50)
    @NonNull
    //@NotBlank(message = "Name is mandatory")
    private String name;

    @Column
    private Integer quantityStudent;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "type_room_id")
    @NonNull
    //@NotBlank
    private TypeRoom typeRoom;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "campus_id")
    @NonNull
    //@NotBlank
    private Campus campus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "price_list_id")
    @NonNull
    //@NotBlank
    private PriceList priceList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
    @JsonIgnore
    private List<Student> students;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "room")
    @JsonIgnore
    private List<PowerBill> powerBills;
}
