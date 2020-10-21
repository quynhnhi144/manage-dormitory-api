package com.managedormitory.converters;

import com.managedormitory.converters.bases.Converter;
import com.managedormitory.exceptions.BadRequestException;
import com.managedormitory.models.dao.Room;
import com.managedormitory.models.dto.RoomDto;
import org.springframework.stereotype.Component;

@Component
public class RoomToRoomDtoConverter extends Converter<Room, RoomDto> {

    @Override
    public RoomDto convert(Room source) throws BadRequestException {
        RoomDto roomDTO = new RoomDto();
        roomDTO.setId(source.getId());
        roomDTO.setName(source.getName());
        roomDTO.setQuantityStudent(source.getQuantityStudent());
        roomDTO.setTypeRoomName(source.getTypeRoom().getName());
        roomDTO.setCampusName(source.getCampus().getName());
        roomDTO.setUserManager(source.getCampus().getUserManager().getFullName());
        if (source.getDetailRoomList().size() == 0) {
            roomDTO.setIsPay(false);
        } else {
            roomDTO.setIsPay(source.getDetailRoomList().get(0).isPay());
        }
        return roomDTO;
    }
}
