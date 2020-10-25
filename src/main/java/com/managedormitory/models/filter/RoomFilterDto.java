package com.managedormitory.models.filter;

import lombok.*;

@Builder
@Getter
@Setter
@ToString
public class RoomFilterDto {
    private String campusName;
    private String roomNameOrUserManager;
    private Integer quantityStudent;
    private String typeRoom;
}
