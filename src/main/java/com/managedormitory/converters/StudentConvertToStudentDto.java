package com.managedormitory.converters;

import com.managedormitory.converters.bases.Converter;
import com.managedormitory.models.dao.Student;
import com.managedormitory.models.dto.StudentDto;
import org.springframework.stereotype.Component;

@Component
public class StudentConvertToStudentDto extends Converter<Student, StudentDto> {

    @Override
    public StudentDto convert(Student source) {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(source.getId());
        studentDto.setName(source.getName());
        studentDto.setPhone(source.getPhone());
        studentDto.setEmail(source.getEmail());
        return studentDto;
    }
}
