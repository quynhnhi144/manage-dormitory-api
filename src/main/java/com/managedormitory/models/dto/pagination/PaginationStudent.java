package com.managedormitory.models.dto.pagination;

import com.managedormitory.models.dto.student.StudentDetailDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationStudent {
    private Map<String, List<StudentDetailDto>> data;
    private int total;
}
