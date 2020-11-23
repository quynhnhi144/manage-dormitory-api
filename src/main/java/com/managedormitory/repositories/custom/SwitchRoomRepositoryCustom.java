package com.managedormitory.repositories.custom;

import com.managedormitory.models.dto.SwitchRoomHistoryDto;
import org.springframework.stereotype.Repository;

@Repository
public interface SwitchRoomRepositoryCustom {

    int addSwitchRoomHistory(SwitchRoomHistoryDto switchRoomHistoryDto);
}
