package com.managedormitory.controllers;

import com.managedormitory.exceptions.BadRequestException;
import com.managedormitory.models.dto.room.RoomBillDto;
import com.managedormitory.services.DetailRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/detailRooms")
public class DetailRoomController {
    @Autowired
    private DetailRoomService detailRoomService;

    @PostMapping("/new-detail-room")
    public int addDetailRoom(@RequestBody RoomBillDto roomBillDto) {
        if (detailRoomService.addDetailRoom(roomBillDto) > 0) {
            return 1;
        }
        throw new BadRequestException("Cannot impelement method!!!");
    }
}
