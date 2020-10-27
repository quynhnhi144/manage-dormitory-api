package com.managedormitory.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {

    private Integer id;

    private String name;

    @NotBlank(message = "Quantity Student is mandatory")
    private Integer quantityStudent;

    @NotBlank(message = "Type Room is mandatory")
    private String typeRoomName;

    @NotBlank(message = "Campus Name is mandatory")
    private String campusName;

    @NotBlank(message = "User Manager is mandatory")
    private String userManager;

    private Boolean isPayRoom;
    private Boolean isPayWaterBill;
    private Boolean isPayVehicleBill;
    private Boolean isPayPowerBill;
}
