package com.managedormitory.converters;

import com.managedormitory.converters.bases.Converter;
import com.managedormitory.exceptions.BadRequestException;
import com.managedormitory.models.dao.Room;
import com.managedormitory.models.dto.RoomDto;
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
            roomDto.setIsPayRoom(false);
            roomDto.setIsPayWaterBill(false);
            roomDto.setIsPayVehicleBill(false);
            roomDto.setIsPayPowerBill(false);

        } else {
            roomDto.setTypeRoomName(source.getTypeRoom().getName());
            roomDto.setIsPayRoom(source.getStudents().get(0).getDetailRoomList().get(0).isPay());
            roomDto.setIsPayWaterBill(source.getStudents().get(0).getDetailRoomList().get(0).isPay());
            roomDto.setIsPayVehicleBill(source.getStudents().get(0).getDetailRoomList().get(0).isPay());
            roomDto.setIsPayPowerBill(source.getStudents().get(0).getDetailRoomList().get(0).isPay());
        }
        return roomDto;
    }
}
