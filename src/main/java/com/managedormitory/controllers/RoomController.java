package com.managedormitory.controllers;

import com.managedormitory.models.dto.*;
import com.managedormitory.models.filter.RoomFilterDto;
import com.managedormitory.services.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @GetMapping
    public PaginationRoom filterRoom(@RequestParam(required = false) String campusName, @RequestParam(required = false) String searchText, @RequestParam(required = false) String typeRoom, @RequestParam(required = false) Integer quantityStudent, @RequestParam int skip, @RequestParam int take) {
        RoomFilterDto roomFilterDto = RoomFilterDto.builder().campusName(campusName).roomNameOrUserManager(searchText).typeRoom(typeRoom).quantityStudent(quantityStudent).build();
        return roomService.paginationGetAllRooms(roomFilterDto, skip, take);
    }

    @GetMapping("/{id}")
    public DetailRoomDto getDetailARoom(@PathVariable Integer id) {
        try {
            return roomService.getRoomById(id);
        } catch (Exception e) {
            e.getMessage();
        }
        return null;
    }
}
