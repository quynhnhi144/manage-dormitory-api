package com.managedormitory.models.dto.room;

import com.managedormitory.models.dao.Room;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomDto {

    private Integer id;
    private String name;
    //@NotBlank(message = "Quantity Student is mandatory")
    private Integer quantityStudent;
    //@NotBlank(message = "Type Room is mandatory")
    private String typeRoomName;
    //@NotBlank(message = "Campus Name is mandatory")
    private String campusName;
    @NonNull
    //@NotBlank(message = "User Manager is mandatory")
    private String userManager;

    private Integer waterPriceId;

    private Integer vehiclePriceId;

    public RoomDto(Room room) {
        this.id = room.getId();
        this.name = room.getName();
        this.quantityStudent = room.getQuantityStudent();
        if (room.getTypeRoom() == null) {
            this.typeRoomName = null;
        } else {
            this.typeRoomName = room.getTypeRoom().getName();
        }
        this.campusName = room.getCampus().getName();
    }
}
