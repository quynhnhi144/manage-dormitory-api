package com.managedormitory.models.dto.room;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomAndCountRegister {
    private DetailRoomDto detailRoomDto;
    private int count;
}
