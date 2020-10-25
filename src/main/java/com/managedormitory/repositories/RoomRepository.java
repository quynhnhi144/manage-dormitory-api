package com.managedormitory.repositories;

import com.managedormitory.models.dao.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository  extends JpaRepository<Room, Integer> {
}
