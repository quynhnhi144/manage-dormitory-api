package com.managedormitory.models.dto.room;

import com.managedormitory.models.dao.TypeRoom;
import com.managedormitory.models.dto.student.StudentInRoomDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailRoomDto {
    private Integer id;
    private String name;
    private Integer quantityStudent;
    private Float priceRoom;
    private Float priceWater;
    private Float priceVehicle;
    private Float pricePower;
    private String campusName;
    private TypeRoom typeRoom;
    private String userManager;
    private List<StudentInRoomDto> students;
    private Boolean isPayRoom;
    private Boolean isPayWaterBill;
    private Boolean isPayVehicleBill;
    private Boolean isPayPowerBill;
}
