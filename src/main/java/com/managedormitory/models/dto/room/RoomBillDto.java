package com.managedormitory.models.dto.room;

import com.managedormitory.models.dto.student.StudentBill;
import com.managedormitory.utils.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomBillDto {
    private Integer billId;
    private String roomName;
    private String studentName;
    private Integer studentId;
    private String studentIdCard;
    private Date startDate;
    private Date endDate;
    private float price;
    private Integer roomId;
    private Integer maxQuantity;

    public RoomBillDto(StudentBill studentBill) {
        this.roomName = studentBill.getRoomName();
        this.studentName = studentBill.getStudentName();
        this.studentId = studentBill.getStudentId();
        this.studentIdCard = studentBill.getStudentIdCard();
        this.startDate = DateUtil.getSDateFromLDate(studentBill.getRoomStartDate());
        this.endDate = DateUtil.getSDateFromLDate(studentBill.getRoomEndDate());
        this.price = studentBill.getMoneyOfRoomMustPay();
        this.roomId = studentBill.getRoomId();
        this.maxQuantity = studentBill.getMaxQuantityStudent();
    }
}
