package com.managedormitory.models.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class StudentDto {
    private Integer id;
    private String name;
    private String phone;
    private String email;
}
