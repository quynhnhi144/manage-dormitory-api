package com.managedormitory.controllers;

import com.managedormitory.converters.bases.Converter;
import com.managedormitory.models.dao.Room;
import com.managedormitory.models.dto.RoomDTO;
import com.managedormitory.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/rooms")
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private Converter<Room, RoomDTO> roomRoomDTOConverter;

    @GetMapping
    public List<RoomDTO> getRoomsByCampusName(@RequestParam(required = false) String campusName, @RequestParam int pageNumber, @RequestParam int pageSize) {
        List<Room> rooms;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        if (campusName == null) {
            rooms = roomService.getAllRooms(pageable);
        } else {

            rooms = roomService.getRoomsByCampus(campusName, pageable);
        }
        return roomRoomDTOConverter.convert(rooms);
    }

}
