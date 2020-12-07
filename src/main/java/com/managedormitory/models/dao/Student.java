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
@Table(name="student", uniqueConstraints = {
        @UniqueConstraint(columnNames = {
                "idCard"
        })})
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 20)
    private Integer id;

    @Column(length = 255)
    private String idCard;

    @Column(length = 100, name = "name", nullable = false)
    @Size(min = 1, max = 50)
    @NonNull
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
    @Column(length = 100)
    private String email;

    @Column
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startingDateOfStay;

    @OneToOne
    @JoinColumn(name = "water_price_id")
    private PriceList priceList;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "room_id")
    @NonNull
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

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "student")
    @JsonIgnore
    private List<SwitchRoomHistory> switchRoomHistories;
}
