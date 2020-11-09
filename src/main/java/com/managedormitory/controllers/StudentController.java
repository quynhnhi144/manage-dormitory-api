package com.managedormitory.controllers;

import com.managedormitory.exceptions.BadRequestException;
import com.managedormitory.models.dto.pagination.PaginationStudent;
import com.managedormitory.models.dto.student.StudentDetailDto;
import com.managedormitory.models.dto.student.StudentUpdateDto;
import com.managedormitory.models.filter.StudentFilterDto;
import com.managedormitory.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/{id}")
    public StudentDetailDto getDetailAStudent(@PathVariable Integer id) {
        try {
            return studentService.getStudentById(id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    @PutMapping("/{id}")
    public StudentDetailDto updateStudent(@PathVariable Integer id, @RequestBody StudentUpdateDto studentUpdateDto) throws BadRequestException {
        return studentService.updateStudent(id, studentUpdateDto);
    }

//    @DeleteMapping("/{id}")
//    public void deleteStudent(@PathVariable Integer id) {
//        studentService.deleteStudent(id);
//    }

}
