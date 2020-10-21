package com.managedormitory.repositories;

import com.managedormitory.models.dao.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Integer> {
    @Query("Select distinct r from Room r left join r.detailRoomList d order by r.id")
    Page<Room> findAllRooms(Pageable pageable);

    @Query("Select distinct r from Room r left join r.detailRoomList d where r.campus.name = :campusName or r.campus.userManager.fullName = :searchText order by r.id desc ")
    Page<Room> findAllRoomsByCampusName(String campusName, String searchText, Pageable pageable);

    @Query("Select distinct r from Room r left join r.detailRoomList d where r.campus.name = :campusName and r.campus.userManager.fullName = :searchText order by r.id desc")
    Page<Room> findAllRoomsByCampusNameAndCampusManager(String campusName, String searchText, Pageable pageable);
}
