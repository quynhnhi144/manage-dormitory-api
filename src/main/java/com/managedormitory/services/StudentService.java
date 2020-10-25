package com.managedormitory.services;

import com.managedormitory.models.dao.Student;
import com.managedormitory.models.dto.PaginationStudent;
import com.managedormitory.models.dto.StudentDto;
import com.managedormitory.models.filter.StudentFilterDto;

import java.util.List;

public interface StudentService {
    List<Student> getAllStudents();
    List<StudentDto> getAllStudentDto();
    PaginationStudent paginationGetAllStudents(StudentFilterDto studentFilterDto, int skip, int take);
    StudentDto getStudentById(Integer id);

}
