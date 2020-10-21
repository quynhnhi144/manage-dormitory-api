package com.managedormitory.models.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DetailRoomMapDB {
    private Integer roomId;
    private String roomName;
    private Integer quantityStudent;
    private Float priceRoom;
    private String campusName;
    private String userManager;
    private String typeRoom;
    private Integer studentId;
    private String studentName;
    private String studentPhoneNumber;
    private String studentEmail;
    private Boolean isPay;
}
