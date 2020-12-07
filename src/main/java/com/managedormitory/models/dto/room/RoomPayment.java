package com.managedormitory.models.dto.room;

import com.managedormitory.models.dto.student.StudentBill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomPayment {
    private Integer roomId;
    private String roomName;
    private List<StudentBill> studentBills;
}
