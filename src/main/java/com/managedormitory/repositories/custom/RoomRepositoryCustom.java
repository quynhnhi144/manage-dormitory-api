package com.managedormitory.repositories.custom;

import com.managedormitory.models.dto.DetailRoomDto;
import com.managedormitory.models.dto.RoomDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepositoryCustom {
    List<RoomDto> getAllRoomByTime();

    int updateQuantityStudent(Integer roomId);

    int updateTypeRoom(Integer id, DetailRoomDto room);
}
