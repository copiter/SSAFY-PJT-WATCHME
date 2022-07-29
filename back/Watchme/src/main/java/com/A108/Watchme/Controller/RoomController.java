package com.A108.Watchme.Controller;

import com.A108.Watchme.DTO.RoomCreateDTO;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping("/addRoom")
    public ApiResponse addRoom(@RequestBody RoomCreateDTO roomCreateDTO){
        return roomService.createRoom(roomCreateDTO);
    }

}
