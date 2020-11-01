package com.managedormitory.models.dto;

import com.managedormitory.models.dao.TypeRoom;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailRoomDto {
    private Integer roomId;
    private String roomName;
    private Integer quantityStudent;
    private Float priceRoom;
    private Float priceWater;
    private Float priceVehicle;
    private Float pricePower;
    private String campusName;
    private TypeRoom typeRoom;
    private String userManager;
    private List<StudentDto> students;
    private Boolean isPayRoom;
    private Boolean isPayWaterBill;
    private Boolean isPayVehicleBill;
    private Boolean isPayPowerBill;
}
