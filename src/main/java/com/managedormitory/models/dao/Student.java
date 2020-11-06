package com.managedormitory.models.dao;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@ToString
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column(length = 100, name = "name", nullable = false)
    @Size(min = 1, max = 50)
    @NonNull
//    //@NotBlank(message = "Full Name is mandatory")
    private String name;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date birthday;

    @Column(length = 100)
    private String address;

    @Column(length = 11, nullable = true)
    private String phone;

    @Email
    @NonNull
//    //@NotBlank(message = "Email is mandatory")
    private String email;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startingDateOfStay;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endingDateOfStay;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "room_id")
    @NonNull
//    //@NotBlank(message = "Room is mandatory")
    private Room room;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "student")
    @JsonIgnore
    private List<DetailRoom> detailRoomList;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "student")
    @JsonIgnore
    private List<WaterBill> waterBills;

    @OneToOne(cascade = CascadeType.MERGE, mappedBy = "studentId")
    @JsonIgnore
    private Vehicle vehicle;
}
