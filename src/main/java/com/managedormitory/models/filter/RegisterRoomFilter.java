package com.managedormitory.models.filter;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Builder
@Getter
@Setter
@ToString
public class RegisterRoomFilter {
    private String campusName;
    private String roomNameOrStudentNameOrIdCard;
}
