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
    private Integer quantityStudent;
    private String typeRoomName;
    private String campusName;
    private String userManager;

    private Integer waterPriceId;

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
