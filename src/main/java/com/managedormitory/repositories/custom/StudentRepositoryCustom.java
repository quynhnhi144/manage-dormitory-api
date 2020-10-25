package com.managedormitory.repositories.custom;

import com.managedormitory.models.dto.StudentDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepositoryCustom {
    List<StudentDto> getAllStudentByTime();
}
