package com.managedormitory.models.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class StudentFilterDto {
    private String campusName;
    private String roomNameOrUserManager;
}
