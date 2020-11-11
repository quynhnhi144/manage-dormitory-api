package com.managedormitory.models.dto.price;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceListDto {
    private Integer id;
    private String name;
    private Float price;
}
