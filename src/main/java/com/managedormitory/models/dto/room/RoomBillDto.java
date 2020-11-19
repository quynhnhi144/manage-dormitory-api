package com.managedormitory.models.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomBillDto {
    private Integer billId;
    private String studentName;
    private Integer studentId;
    private Date startDate;
    private Date endDate;
    private float price;
    private Integer roomId;
    private Integer maxQuantity;
}
