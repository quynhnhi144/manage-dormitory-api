package com.managedormitory.models.dao;

import lombok.*;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String idCard;

    @Column
    private String studentName;

    @Column
    private Date birthday;

    @Column
    private String address;

    @Column
    private String phone;

    @Column
    private String email;

    @Column
    private Date startingDateOfStay;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinColumn(name = "room_id")
    @NonNull
    private Room room;
}
