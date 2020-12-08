package com.managedormitory.controllers;

import com.managedormitory.exceptions.NotFoundException;
import com.managedormitory.models.dto.room.DetailRoomDto;
import com.managedormitory.models.dto.pagination.PaginationRoom;
import com.managedormitory.models.dto.room.RoomAndCountRegister;
import com.managedormitory.models.dto.room.RoomPayment;
import com.managedormitory.models.dto.student.StudentBill;
import com.managedormitory.models.dto.student.StudentLeftDto;
import com.managedormitory.models.filter.RoomFilterDto;
import com.managedormitory.services.RoomService;
import com.managedormitory.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("api/rooms")
public class RoomController {
    @Autowired
    private RoomService roomService;

    @GetMapping
    public PaginationRoom filterRoom(@RequestParam(required = false) String campusName, @RequestParam(required = false) String searchText, @RequestParam(required = false) Integer typeRoomId, @RequestParam(required = false) Integer quantityStudent, @RequestParam int skip, @RequestParam int take) {
        RoomFilterDto roomFilterDto = RoomFilterDto.builder().campusName(campusName).roomNameOrUserManager(searchText).typeRoomId(typeRoomId).quantityStudent(quantityStudent).build();
        return roomService.paginationGetAllRooms(roomFilterDto, skip, take);
    }

    @GetMapping("/all")
    public List<DetailRoomDto> getAllRooms() {
        return roomService.getAllDetailRoomDto();
    }

    @GetMapping("/remaining-room")
    public List<RoomAndCountRegister> getAllRemainingRooms(@RequestParam(required = false) String searchText) {
        return roomService.getAllRemainingRoomDto(searchText);
    }

    @GetMapping("/enough-condition-switch-room")
    public List<DetailRoomDto> getEnoughConditionSwitchRooms() {
        return roomService.getEnoughConditionSwitchRooms();
    }

    @GetMapping("/{id}")
    public DetailRoomDto getDetailARoom(@PathVariable Integer id) {
        try {
            return roomService.getRoomById(id);
        } catch (Exception e) {
            throw new NotFoundException("Cannot find the Id: " + id);
        }
    }

    @GetMapping("/export")
    public ResponseEntity<Resource> exportToExcel() {
        try {
            InputStreamResource file = new InputStreamResource(roomService.exportExcelRooms());
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + StringUtil.FILE_NAME_EXCEL_ROOM)
                    .contentType(MediaType.parseMediaType("application/vnd.ms-excel"))
                    .body(file);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @GetMapping("/all-bill/{id}")
    public RoomPayment getPaymentOfAllStudentsInRoom(@PathVariable Integer id) {
        return roomService.getPaymentOfAllStudentsInRoom(id);
    }

    @PutMapping("/{id}")
    public DetailRoomDto updateTypeRoom(@PathVariable Integer id, @RequestBody DetailRoomDto detailRoomDto) {
        return roomService.updateTypeRoom(id, detailRoomDto);
    }
}
