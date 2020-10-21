package com.managedormitory.models.dto;

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
    private String campusName;
    private String typeRoom;
    private String userManager;
    private List<StudentDto> students;
    private boolean isPay;
}
