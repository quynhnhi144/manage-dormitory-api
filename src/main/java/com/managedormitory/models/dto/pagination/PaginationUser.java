package com.managedormitory.models.dto.pagination;

import com.managedormitory.models.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationUser {
    private Map<String, List<UserDto>> data;
    private int total;
}