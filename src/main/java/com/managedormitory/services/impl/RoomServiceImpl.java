package com.managedormitory.services.impl;

import com.managedormitory.models.dao.Room;
import com.managedormitory.repositories.RoomRepository;
import com.managedormitory.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    RoomRepository roomRepository;

    @Override
    public Page<Room> getAllRooms(Pageable pageable) {
        return roomRepository.findAllRooms(pageable);
    }

    @Override
    public Page<Room> getRoomsByCampus(String campusName, String searchText, Pageable pageable) {
        return roomRepository.findAllRoomsByCampusName(campusName, searchText, pageable);
    }

    @Override
    public Page<Room> getRoomsByCampusAndCampusManager(String campusName, String searchText, Pageable pageable) {
        return roomRepository.findAllRoomsByCampusName(campusName, searchText, pageable);
    }
}
