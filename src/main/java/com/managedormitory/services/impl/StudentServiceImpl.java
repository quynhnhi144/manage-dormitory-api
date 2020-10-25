package com.managedormitory.services.impl;

import com.managedormitory.exceptions.NotFoundException;
import com.managedormitory.models.dao.DetailRoom;
import com.managedormitory.models.dao.Student;
import com.managedormitory.models.dto.PaginationStudent;
import com.managedormitory.models.dto.StudentDto;
import com.managedormitory.models.dto.StudentFilterDto;
import com.managedormitory.repositories.StudentRepository;
import com.managedormitory.repositories.custom.StudentRepositoryCustom;
import com.managedormitory.services.StudentService;
import com.managedormitory.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentRepositoryCustom studentRepositoryCustom;

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    @Override
    public List<StudentDto> getAllStudentDto() {
        List<Student> students = getAllStudents();
        List<StudentDto> studentDtos = studentRepositoryCustom.getAllStudentByTime();
        List<StudentDto> studentDtosDetail = new ArrayList<>();
        List<Integer> studentDtosIdList = studentDtos.stream().mapToInt(StudentDto::getId).boxed().collect(Collectors.toList());
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            StudentDto studentDto = new StudentDto();
            studentDto.setId(student.getId());
            studentDto.setName(student.getName());
            studentDto.setBirthday(student.getBirthday());
            studentDto.setPhone(student.getPhone());
            studentDto.setEmail(student.getEmail());
            studentDto.setAddress(student.getAddress());
            studentDto.setStartingDateOfStay(DateUtil.getSDateFromLDate(student.getStartingDateOfStay()));
            studentDto.setEndingDateOfStay(DateUtil.getSDateFromLDate(student.getEndingDateOfStay()));
            studentDto.setRoomName(student.getRoom().getName());
            studentDto.setCampusName(student.getRoom().getCampus().getName());
            if (student.getRoom().getTypeRoom() == null) {
                studentDto.setTypeRoom(null);
            } else {
                studentDto.setTypeRoom(student.getRoom().getTypeRoom().getName());
            }
            studentDto.setUserManager(student.getRoom().getCampus().getUserManager().getFullName());

            if (studentDtosIdList.contains(student.getId())) {
                studentDto.setIsPayRoom(true);
                studentDto.setIsPayWaterBill(true);
                studentDto.setIsPayVehicleBill(true);
                studentDto.setIsPayPowerBill(true);
            } else {
                studentDto.setIsPayRoom(false);
                studentDto.setIsPayWaterBill(false);
                studentDto.setIsPayVehicleBill(false);
                studentDto.setIsPayPowerBill(false);
            }
            studentDtosDetail.add(studentDto);
        }
        return studentDtosDetail;
    }

    @Override
    public PaginationStudent paginationGetAllStudents(StudentFilterDto studentFilterDto, int skip, int take) {
        List<StudentDto> studentDtos = getAllStudentDto();
        if (studentFilterDto.getCampusName() != null) {
            studentDtos = studentDtos.stream()
                    .filter(studentDto -> studentDto.getCampusName().equals(studentFilterDto.getCampusName()))
                    .collect(Collectors.toList());
        }
        if (studentFilterDto.getRoomNameOrUserManager() != null) {
            if (!studentFilterDto.getRoomNameOrUserManager().equals("")) {
                studentDtos = studentDtos.stream()
                        .filter(studentDto -> studentDto.getRoomName().equals(studentFilterDto.getRoomNameOrUserManager()) || studentDto.getUserManager().equals(studentFilterDto.getRoomNameOrUserManager()))
                        .collect(Collectors.toList());
            }
        }

        int total = studentDtos.size();
        int lastElement;
        if (take < total) {
            if (skip + take < total) {
                lastElement = skip + take;
            } else {
                lastElement = total;
            }
        } else {
            lastElement = total;
        }
        Map<String, List<StudentDto>> studentDtoMap = new HashMap<>();
        studentDtoMap.put("data", studentDtos.subList(skip, lastElement));
        return new PaginationStudent(studentDtoMap, total);
    }

    @Override
    public StudentDto getStudentById(Integer id) {
        List<StudentDto> studentDtos = getAllStudentDto();
        List<StudentDto> studentDtoById = studentDtos.stream().filter(studentDto -> studentDto.getId().equals(id)).collect(Collectors.toList());
        if (studentDtoById.size() == 0) {
            throw new NotFoundException("Cannot find Student Id: " + id);
        }
        return studentDtoById.get(0);
    }
}
