package com.managedormitory.repositories.custom;

import com.managedormitory.models.dto.room.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoomRepositoryCustom {
    List<RoomDto> getAllRoomByTime();

    int updateQuantityStudent(Integer roomId);

    int updateTypeRoom(Integer id, DetailRoomDto room);

    Optional<InfoMoney> getInfoLatestBillForNewStudent(Integer roomId);

    Optional<RoomBillDto> getDetailRoomRecently(Integer studentId);
}
