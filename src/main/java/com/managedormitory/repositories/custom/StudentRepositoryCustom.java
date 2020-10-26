package com.managedormitory.repositories.custom;

import com.managedormitory.models.dto.StudentDetailDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepositoryCustom {
    List<StudentDetailDto> getAllStudentByTime();
}
