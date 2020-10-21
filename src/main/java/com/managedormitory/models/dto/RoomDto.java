package com.managedormitory.models.dto;

import com.managedormitory.models.dao.Student;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {

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

    private Boolean isPay;
}
