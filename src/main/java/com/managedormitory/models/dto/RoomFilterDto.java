package com.managedormitory.models.dto;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
public class RoomFilterDto {
    private String campusName;
    private String userManager;
    private Integer quantityStudent;
    private String typeRoom;
}
