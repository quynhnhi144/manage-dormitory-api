package com.managedormitory.models.dto;

import com.managedormitory.models.dao.Student;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class RoomHasStudents {
    private Integer id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Quantity Student is mandatory")
    private Integer quantityStudent;

    @NotBlank(message = "Type Room is mandatory")
    private String typeRoomName;

    @NotBlank(message = "Campus Name is mandatory")
    private String campusName;

    @NotBlank(message = "User Manager is mandatory")
    private String userManager;

    private List<Student> studentList;
}
