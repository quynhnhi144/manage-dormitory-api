package com.managedormitory.models.dto.vehicle;

import com.managedormitory.models.dao.TypeVehicle;
import com.managedormitory.models.dto.student.StudentDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VehicleDetailDto {
    private Integer id;
    private String licensePlates;
    private TypeVehicle typeVehicle;
    private StudentDto studentDto;
    private String roomName;
    private String campusName;
    private String userManager;
    private Date startDate;
    private Date endDate;
    private boolean isPayVehicleBill;
    private boolean isActive;
}
