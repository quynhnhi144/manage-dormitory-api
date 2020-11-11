package com.managedormitory.repositories.custom;

import com.managedormitory.models.dto.student.StudentDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepositoryCustom {
    List<StudentDto> getAllStudentByTime();
    int updateStudent(Integer id, StudentDto studentDto);
    int updateRoomIdOfStudent(Integer studentId, Integer roomId);
}
