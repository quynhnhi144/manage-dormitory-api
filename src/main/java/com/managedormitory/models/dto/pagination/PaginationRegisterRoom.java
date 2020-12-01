package com.managedormitory.models.dto.pagination;

import com.managedormitory.models.dto.registerRoom.RegisterRoomDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRegisterRoom {
    private Map<String, List<RegisterRoomDto>> data;
    private int total;
}
