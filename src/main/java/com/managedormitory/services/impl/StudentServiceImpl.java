package com.managedormitory.services.impl;

import com.managedormitory.exceptions.NotFoundException;
import com.managedormitory.models.dao.Student;
import com.managedormitory.models.dto.PaginationStudent;
import com.managedormitory.models.dto.StudentDetailDto;
import com.managedormitory.models.filter.StudentFilterDto;
import com.managedormitory.repositories.StudentRepository;
import com.managedormitory.repositories.custom.StudentRepositoryCustom;
import com.managedormitory.services.StudentService;
import com.managedormitory.utils.DateUtil;
import com.managedormitory.utils.PaginationUtils;
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
    public List<StudentDetailDto> getAllStudentDto() {
        List<Student> students = getAllStudents();
        List<StudentDetailDto> studentDetailDtos = studentRepositoryCustom.getAllStudentByTime();
        List<StudentDetailDto> studentDetailDtosDetail = new ArrayList<>();
        List<Integer> studentDtosIdList = studentDetailDtos.stream().mapToInt(StudentDetailDto::getId).boxed().collect(Collectors.toList());
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            StudentDetailDto studentDetailDto = new StudentDetailDto();
            studentDetailDto.setId(student.getId());
            studentDetailDto.setName(student.getName());
            studentDetailDto.setBirthday(student.getBirthday());
            studentDetailDto.setPhone(student.getPhone());
            studentDetailDto.setEmail(student.getEmail());
            studentDetailDto.setAddress(student.getAddress());
            studentDetailDto.setStartingDateOfStay(DateUtil.getSDateFromLDate(student.getStartingDateOfStay()));
            studentDetailDto.setEndingDateOfStay(DateUtil.getSDateFromLDate(student.getEndingDateOfStay()));
            studentDetailDto.setRoomName(student.getRoom().getName());
            studentDetailDto.setCampusName(student.getRoom().getCampus().getName());
            if (student.getRoom().getTypeRoom() == null) {
                studentDetailDto.setTypeRoom(null);
            } else {
                studentDetailDto.setTypeRoom(student.getRoom().getTypeRoom().getName());
            }
            studentDetailDto.setUserManager(student.getRoom().getCampus().getUserManager().getFullName());

            if (studentDtosIdList.contains(student.getId())) {
                studentDetailDto.setIsPayRoom(true);
                studentDetailDto.setIsPayWaterBill(true);
                studentDetailDto.setIsPayVehicleBill(true);
                studentDetailDto.setIsPayPowerBill(true);
            } else {
                studentDetailDto.setIsPayRoom(false);
                studentDetailDto.setIsPayWaterBill(false);
                studentDetailDto.setIsPayVehicleBill(false);
                studentDetailDto.setIsPayPowerBill(false);
            }
            studentDetailDtosDetail.add(studentDetailDto);
        }
        return studentDetailDtosDetail;
    }

    @Override
    public PaginationStudent paginationGetAllStudents(StudentFilterDto studentFilterDto, int skip, int take) {
        List<StudentDetailDto> studentDetailDtos = getAllStudentDto();
        if (studentFilterDto.getCampusName() != null) {
            studentDetailDtos = studentDetailDtos.stream()
                    .filter(studentDto -> studentDto.getCampusName().toLowerCase().equals(studentFilterDto.getCampusName().toLowerCase()))
                    .collect(Collectors.toList());
        }
        if (studentFilterDto.getStudentNameOrRoomNameOrUserManager() != null) {
            String searchText = studentFilterDto.getStudentNameOrRoomNameOrUserManager().toLowerCase();
            if (!searchText.equals("")) {
                studentDetailDtos = studentDetailDtos.stream()
                        .filter(studentDto -> studentDto.getRoomName().toLowerCase().equals(searchText)
                                || studentDto.getUserManager().toLowerCase().equals(searchText)
                                || studentDto.getName().toLowerCase().equals(searchText))
                        .collect(Collectors.toList());
            }
        }

        int total = studentDetailDtos.size();
        int lastElement = PaginationUtils.getLastElement(skip, take, total);
        Map<String, List<StudentDetailDto>> studentDtoMap = new HashMap<>();
        studentDtoMap.put("data", studentDetailDtos.subList(skip, lastElement));
        return new PaginationStudent(studentDtoMap, total);
    }

    @Override
    public StudentDetailDto getStudentById(Integer id) {
        List<StudentDetailDto> studentDetailDtos = getAllStudentDto();
        List<StudentDetailDto> studentDetailDtoById = studentDetailDtos.stream()
                .filter(studentDto -> studentDto.getId().equals(id))
                .collect(Collectors.toList());
        if (studentDetailDtoById.size() == 0) {
            throw new NotFoundException("Cannot find Student Id: " + id);
        }
        return studentDetailDtoById.get(0);
    }
}
