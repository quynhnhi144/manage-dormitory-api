package com.managedormitory.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
    private Integer id;
    private String username;
    private String fullName;
    private LocalDate birthday;
    private String email;
    private String address;
    private String phone;
    private List<CampusDto> campuses;
}
