package com.managedormitory.models.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="vehicle")
public class Vehicle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(length = 20)
    private Integer id;

    @Column(length = 20)
    @NonNull
    private String licensePlates;

    @OneToOne
    @JoinColumn(name="vehicle_price_id")
    private PriceList priceList;

    @OneToOne
    @JoinColumn(name = "type_vehicle_id")
    @NonNull
//    //@NotBlank
    private TypeVehicle typeVehicleId;

    @OneToOne
    @JoinColumn(name = "student_id")
//    //@NotBlank
    private Student studentId;

    @OneToMany(cascade = CascadeType.MERGE, mappedBy = "vehicle")
    @JsonIgnore
    private List<VehicleBill> vehicleBills;

}
