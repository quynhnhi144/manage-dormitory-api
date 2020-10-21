package com.managedormitory.services;

import com.managedormitory.models.dao.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RoomService {
    Page<Room> getAllRooms(Pageable pageable);
    Page<Room> getRoomsByCampus(String campusName, String searchText, Pageable pageable);
    Page<Room> getRoomsByCampusAndCampusManager(String campusName, String searchText, Pageable pageable);
}
