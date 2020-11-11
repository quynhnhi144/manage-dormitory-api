package com.managedormitory.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DashBoard {

    private int totalNumberUser;
    private int totalNumberCampus;
    private int totalNumberRoom;
    private int totalNumberVehicle;
    private int totalNumberStudent;
    private int totalNumberRemainingRoom;
}
