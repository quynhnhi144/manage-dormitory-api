package com.managedormitory.models.dto;

import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRoom {
    private Map<String, List<DetailRoomDto>> data;
    private int total;
}
