package com.managedormitory.converters;

import com.managedormitory.converters.bases.Converter;
import com.managedormitory.models.dao.Student;
import com.managedormitory.models.dto.student.StudentDto;
import com.managedormitory.utils.DateUtil;
import org.springframework.stereotype.Component;

@Component
public class StudentConvertToStudentDto extends Converter<Student, StudentDto> {

    @Override
    public StudentDto convert(Student source) {
        StudentDto studentDto = new StudentDto();
        studentDto.setId(source.getId());
        studentDto.setIdCard(source.getIdCard());
        studentDto.setName(source.getName());
        studentDto.setBirthday(source.getBirthday());
        studentDto.setPhone(source.getPhone());
        studentDto.setEmail(source.getEmail());
        studentDto.setAddress(source.getAddress());
        studentDto.setStartingDateOfStay(DateUtil.getSDateFromLDate(source.getStartingDateOfStay()));
        studentDto.setRoomId(source.getRoom().getId());
        return studentDto;
    }
}
