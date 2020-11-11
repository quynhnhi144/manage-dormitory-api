package com.managedormitory.converters;

import com.managedormitory.converters.bases.Converter;
import com.managedormitory.exceptions.BadRequestException;
import com.managedormitory.models.dao.Room;
import com.managedormitory.models.dto.room.RoomDto;
import org.springframework.stereotype.Component;

@Component
public class RoomConvertToRoomDto extends Converter<Room, RoomDto> {
    @Override
    public RoomDto convert(Room source) throws BadRequestException {
        RoomDto roomDto = new RoomDto();
        roomDto.setId(source.getId());
        roomDto.setName(source.getName());
        roomDto.setQuantityStudent(source.getQuantityStudent());
        roomDto.setCampusName(source.getCampus().getName());
        roomDto.setUserManager(source.getCampus().getUserManager().getFullName());
        if (source.getQuantityStudent() == 0) {
            roomDto.setTypeRoomName(null);

        } else {
            roomDto.setTypeRoomName(source.getTypeRoom().getName());
        }
        return roomDto;
    }
}
