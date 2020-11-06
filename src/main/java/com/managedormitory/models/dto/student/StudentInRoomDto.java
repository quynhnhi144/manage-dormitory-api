package com.managedormitory.models.dto.student;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class StudentInRoomDto {
    private Integer id;
    private String name;
    private String phone;
    private String email;
}
