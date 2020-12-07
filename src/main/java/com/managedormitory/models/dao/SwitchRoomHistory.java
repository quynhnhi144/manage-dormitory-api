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
    @Column(length = 20)
    private Integer id;

    @Column(length = 45)
    private String oldRoomName;

    @Column(length = 45)
    private String newRoomName;

    @Column(length = 20)
    private float roomMoney;

    @Column(length = 20)
    private float waterMoney;

    @Column(length = 20)
    private float vehicleMoney;

    @ManyToOne
    @MapsId("studentId")
    @JoinColumn(name = "student_id")
    private Student student;

    @Column
    @NonNull
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate createDate;
}
