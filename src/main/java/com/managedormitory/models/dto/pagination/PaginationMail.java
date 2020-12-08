package com.managedormitory.models.dto.pagination;

import com.managedormitory.models.dao.Mail;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationMail {
    private Map<String, List<Mail>> data;
    private int total;
}
