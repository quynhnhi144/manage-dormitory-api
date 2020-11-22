package com.managedormitory.repositories.custom;

import com.managedormitory.models.dto.room.RoomBillDto;
import org.springframework.stereotype.Repository;

@Repository
public interface DetailRoomRepositoryCustom {
    int addDetailRoom(RoomBillDto roomBillDto);
}
