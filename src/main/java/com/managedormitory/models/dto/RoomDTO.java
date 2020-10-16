package com.managedormitory.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDTO {

    private Integer id;

    @NotBlank(message = "Name is mandatory")
    private String name;

    @NotBlank(message = "Quantity Student is mandatory")
    private Integer quantityStudent;

    @NotBlank(message = "Type Room is mandatory")
    private String typeRoomName;

    @NotBlank(message = "Campus Name is mandatory")
    private String campusName;

    private Float price;
}
