package com.managedormitory.models.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SwitchRoomHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    private String oldRoomName;

    @Column
    private String newRoomName;

    @Column
    private float givingRoomMoney;

    @Column
    private float takingRoomMoney;

    @Column
    private float givingWaterMoney;

    @Column
    private float takingWaterMoney;

    @Column
    private float givingVehicleMoney;

    @Column
    private float takingVehicleMoney;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private Student student;

    @Column
    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createDate;
}
