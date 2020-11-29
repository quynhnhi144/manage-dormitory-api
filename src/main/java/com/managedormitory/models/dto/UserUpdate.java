package com.managedormitory.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserUpdate {
    private UserDto userDto;
    private String oldPassword;
    private String newPassword;
}
