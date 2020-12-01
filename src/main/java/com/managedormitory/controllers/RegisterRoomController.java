package com.managedormitory.controllers;

import com.managedormitory.models.dto.pagination.PaginationRegisterRoom;
import com.managedormitory.models.dto.registerRoom.RegisterRoomDto;
import com.managedormitory.models.dto.registerRoom.RegisterRoomIncludePayment;
import com.managedormitory.models.dto.student.StudentNewDto;
import com.managedormitory.models.filter.RegisterRoomFilter;
import com.managedormitory.services.RegisterRoomService;
import com.managedormitory.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("api/registerRooms")
public class RegisterRoomController {
    @Autowired
    private StudentService studentService;

    @Autowired
    private RegisterRoomService registerRoomService;

    @GetMapping()
    public PaginationRegisterRoom filterRegisterRoom(@RequestParam(required = false) String campusName, @RequestParam int skip, @RequestParam int take, @RequestParam(required = false) String searchText) {
        RegisterRoomFilter registerRoomFilter = RegisterRoomFilter.builder().campusName(campusName).roomNameOrStudentNameOrIdCard(searchText).build();
        return registerRoomService.paginationGetAllRegisterRoom(registerRoomFilter, skip, take);
    }

    @GetMapping("/{id}")
    public RegisterRoomIncludePayment getRegisterRoomPayment(@PathVariable Integer id) {
        return registerRoomService.getRegisterRoom(id);
    }

    @PostMapping()
    public int registerRemainingRoomForStudent(@RequestBody RegisterRoomDto registerRoomDto) {
        return studentService.registerRemainingRoomForStudent(registerRoomDto);
    }

    @PostMapping("/{id}/addStudent")
    public int addStudentFromRegisterRoom(@PathVariable Integer id, @RequestBody StudentNewDto studentNewDto) {
        return registerRoomService.addStudentFromRegisterRoom(id, studentNewDto);
    }
}
