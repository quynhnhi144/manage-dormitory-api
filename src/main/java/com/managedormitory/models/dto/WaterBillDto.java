package com.managedormitory.models.dto;

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
public class WaterBillDto {
    private Integer billId;
    private String studentName;
    private Integer studentId;
    private Date startDate;
    private Date endDate;
    private float price;
    private Integer roomId;

    public WaterBillDto(StudentBill studentBill) {
        this.studentName = studentBill.getStudentName();
        this.studentId = studentBill.getStudentId();
        this.startDate = DateUtil.getSDateFromLDate(studentBill.getWaterStartDate());
        this.endDate = DateUtil.getSDateFromLDate(studentBill.getWaterEndDate());
        this.price = studentBill.getMoneyOfWaterMustPay();
        this.roomId = studentBill.getRoomId();
    }

}
