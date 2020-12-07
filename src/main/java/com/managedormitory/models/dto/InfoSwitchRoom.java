package com.managedormitory.models.dto;

import com.managedormitory.models.dto.room.RoomBillDto;
import com.managedormitory.models.dto.vehicle.VehicleBillDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InfoSwitchRoom {
    private Integer studentId;
    private String studentIdCard;
    private String studentName;
    private Integer oldRoomId;
    private String oldRoomName;
    private Integer newRoomId;
    private String newRoomName;
    private Integer maxQuantityStudent;
    private RoomBillDto roomBill;
    private WaterBillDto waterBill;
    private VehicleBillDto vehicleBill;
}
