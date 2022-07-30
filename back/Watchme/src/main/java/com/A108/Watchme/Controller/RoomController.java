package com.A108.Watchme.Controller;

import com.A108.Watchme.DTO.PostRoomReqDTO;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping("/addRoom")
    public ApiResponse addRoom(@RequestBody PostRoomReqDTO postRoomReqDTO) {
        return roomService.createRoom(postRoomReqDTO);
    }

    @GetMapping("/room/recruit")
    public ApiResponse getRoom(@RequestParam(required = false, value="category") String ctgName, @RequestParam(value="page") int page, HttpServletRequest request) {
        return roomService.getRoom(ctgName, page);
    }

    @PostMapping("/room/join/{roomId}")
    public ApiResponse joinRoom(@PathVariable("roomId") int roomId){
        return roomService.joinRoom(Long.valueOf(roomId));
    }

}

