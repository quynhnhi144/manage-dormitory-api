package com.managedormitory.converters;

import com.managedormitory.converters.bases.Converter;
import com.managedormitory.exceptions.BadRequestException;
import com.managedormitory.models.dao.Room;
import com.managedormitory.models.dto.RoomDTO;
import org.springframework.stereotype.Component;

@Component
public class RoomToRoomDTOConverter extends Converter<Room, RoomDTO> {

    @Override
    public RoomDTO convert(Room source) throws BadRequestException {
        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(source.getId());
        roomDTO.setName(source.getName());
        roomDTO.setQuantityStudent(source.getQuantityStudent());
        roomDTO.setTypeRoomName(source.getTypeRoom().getName());
        roomDTO.setCampusName(source.getCampus().getName());
        roomDTO.setPrice(source.getPriceList().getPrice());

        return roomDTO;
    }


}
