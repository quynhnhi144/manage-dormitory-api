package com.managedormitory.services;

import com.managedormitory.models.dao.Room;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoomService {
    List<Room> getAllRooms(Pageable pageable);
    List<Room> getRoomsByCampus(String campusName, Pageable pageable);
}
