package com.managedormitory.controllers;

import com.managedormitory.exceptions.NotFoundException;
import com.managedormitory.models.dao.TypeRoom;
import com.managedormitory.services.TypeRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("api/typeRoom")
public class TypeRoomController {
    @Autowired
    private TypeRoomService typeRoomService;

    @GetMapping("/{id}")
    public ResponseEntity<TypeRoom> getTypeRoom(@PathVariable Integer id){
        Optional<TypeRoom> typeRoom = typeRoomService.getTypeRoomById(id);
        if (!typeRoom.isPresent()) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(typeRoom.get(), HttpStatus.OK);
    }
}
