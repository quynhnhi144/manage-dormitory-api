package com.managedormitory.controllers;

import com.managedormitory.exceptions.BadRequestException;
import com.managedormitory.exceptions.NotFoundException;
import com.managedormitory.models.dto.pagination.PaginationStudent;
import com.managedormitory.models.dto.room.RoomBillDto;
import com.managedormitory.models.dto.room.RoomPriceAndWaterPriceDto;
import com.managedormitory.models.dto.student.StudentDetailDto;
import com.managedormitory.models.dto.student.StudentDto;
import com.managedormitory.models.dto.student.StudentMoveDto;
import com.managedormitory.models.dto.student.StudentNewDto;
import com.managedormitory.models.filter.StudentFilterDto;
import com.managedormitory.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    public PaginationStudent filterStudent(@RequestParam(required = false) String campusName, @RequestParam(required = false) String searchText, @RequestParam int skip, @RequestParam int take) {
        StudentFilterDto studentFilterDto = StudentFilterDto.builder().campusName(campusName).studentNameOrRoomNameOrUserManager(searchText).build();
        return studentService.paginationGetAllStudents(studentFilterDto, skip, take);
    }

    @GetMapping("/all")
    public List<StudentDetailDto> getAllStudents() {
        return studentService.getAllStudentDto();
    }

    @GetMapping("/{id}")
    public StudentDetailDto getDetailAStudent(@PathVariable Integer id) {
        try {
            return studentService.getStudentById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @GetMapping("/{id}/studentLeft")
    public StudentMoveDto getRoomBillDto(@PathVariable Integer id) {
        try {
            return studentService.getInfoMovingStudent(id);
        } catch (Exception e) {
            throw new NotFoundException("Cannot find this " + id);
        }
    }

    @GetMapping("/{roomId}/money-room-and-money-water")
    public RoomPriceAndWaterPriceDto getRoomPriceAndWaterPrice(@PathVariable Integer roomId) {
        return studentService.getRoomPriceAndWaterPrice(roomId);
    }

    @PostMapping("/addStudent")
    public StudentDto addStudent(@RequestBody StudentNewDto studentNewDto) {
        return studentService.addStudent(studentNewDto);
    }

    @PostMapping("/studentLeft")
    public int addStudentLeft(@RequestBody StudentMoveDto studentMoveDto) {
        return studentService.addStudentLeft(studentMoveDto);
    }

    @PutMapping("/{id}")
    public StudentDetailDto updateStudent(@PathVariable Integer id, @RequestBody StudentDto studentDto) throws BadRequestException {
        return studentService.updateStudent(id, studentDto);
    }
}
