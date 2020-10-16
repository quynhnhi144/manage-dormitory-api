package com.managedormitory.services.impl;

import com.managedormitory.models.dao.Room;
import com.managedormitory.repositories.RoomRepository;
import com.managedormitory.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoomServiceImpl implements RoomService {

    @Autowired
    RoomRepository roomRepository;

    @Override
    public List<Room> getAllRooms(Pageable pageable) {
        return roomRepository.findAll(pageable).getContent();
    }

    @Override
    public List<Room> getRoomsByCampus(String campusName, Pageable pageable) {
        return roomRepository.findByCampus_Name(campusName, pageable);
    }
}
