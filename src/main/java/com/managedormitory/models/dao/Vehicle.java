package com.managedormitory.models.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Integer id;

    @Column
    @NonNull
    private String licensePlates;

    @OneToOne
    @JoinColumn(name = "type_vehicle_id")
    @NonNull
    @NotBlank
    private TypeVehicle typeVehicleId;

    @OneToOne
    @JoinColumn(name = "student_id")
    @NonNull
    @NotBlank
    private Student studentId;

}
