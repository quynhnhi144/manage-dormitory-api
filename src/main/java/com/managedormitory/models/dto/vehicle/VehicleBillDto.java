package com.managedormitory.models.dto.vehicle;

import com.managedormitory.models.dto.student.StudentBill;
import com.managedormitory.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VehicleBillDto {
    private Integer billId;
    private String studentName;
    private Integer studentId;
    private String studentIdCard;
    private Date startDate;
    private Date endDate;
    private float price;
    private Integer roomId;
    private Integer vehicleId;

    public VehicleBillDto(StudentBill studentBill) {
        this.studentName = studentBill.getStudentName();
        this.studentId = studentBill.getStudentId();
        this.studentIdCard = studentBill.getStudentIdCard();
        this.startDate = DateUtil.getSDateFromLDate(studentBill.getVehicleStartDate());
        this.endDate = DateUtil.getSDateFromLDate(studentBill.getVehicleEndDate());
        this.price = studentBill.getMoneyOfVehicleMustPay();
        this.roomId = studentBill.getRoomId();
        this.vehicleId = studentBill.getVehicleId();
    }
}