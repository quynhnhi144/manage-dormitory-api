package com.managedormitory.models.filter;

import com.managedormitory.models.dao.TypeRoom;
import lombok.*;

@Builder
@Getter
@Setter
@ToString
public class RoomFilterDto {
    private String campusName;
    private String roomNameOrUserManager;
    private Integer quantityStudent;
    private Integer typeRoomId;
}
