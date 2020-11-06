package com.managedormitory.converters;

import com.managedormitory.converters.bases.Converter;
import com.managedormitory.models.dao.Student;
import com.managedormitory.models.dto.student.StudentInRoomDto;
import org.springframework.stereotype.Component;

@Component
public class StudentConvertToStudentDto extends Converter<Student, StudentInRoomDto> {

    @Override
    public StudentInRoomDto convert(Student source) {
        StudentInRoomDto studentInRoomDto = new StudentInRoomDto();
        studentInRoomDto.setId(source.getId());
        studentInRoomDto.setName(source.getName());
        studentInRoomDto.setPhone(source.getPhone());
        studentInRoomDto.setEmail(source.getEmail());
        return studentInRoomDto;
    }
}
