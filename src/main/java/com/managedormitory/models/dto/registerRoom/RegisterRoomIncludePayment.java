package com.managedormitory.models.dto.registerRoom;

import com.managedormitory.models.dto.room.InfoMoneyDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RegisterRoomIncludePayment {
    private RegisterRoomDto registerRoomDto;
    private InfoMoneyDto infoMoneyDto;
}
