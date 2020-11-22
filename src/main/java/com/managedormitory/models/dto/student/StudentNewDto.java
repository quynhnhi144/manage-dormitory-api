package com.managedormitory.models.dto.student;

import com.managedormitory.models.dto.WaterBillDto;
import com.managedormitory.models.dto.room.RoomBillDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentNewDto {
    private StudentDto studentDto;
    private WaterBillDto waterBillDto;
    private RoomBillDto roomBillDto;
}
