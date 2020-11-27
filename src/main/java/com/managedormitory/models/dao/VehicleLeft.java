package com.managedormitory.models.dao;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class VehicleLeft {

    @Id
    private Integer id;

    @Column()
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate leavingDate;

    @Column
    private float numberOfGiveOfTakeVehicleMoney;

    @OneToOne
    @PrimaryKeyJoinColumn(name = "id", referencedColumnName = "id")
    private Vehicle vehicle;
}
