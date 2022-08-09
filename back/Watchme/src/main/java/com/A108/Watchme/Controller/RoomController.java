package com.A108.Watchme.Controller;

import com.A108.Watchme.DTO.*;
import com.A108.Watchme.DTO.Room.JoinRoomDTO;
import com.A108.Watchme.DTO.Room.RoomUpdateDTO;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Service.RoomService;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RoomController {

    @Autowired
    private RoomService roomService;

    @PostMapping(value = "/rooms", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse addRoom(@RequestPart PostRoomReqDTO postRoomReqDTO, @RequestPart(required = false) MultipartFile images, HttpServletRequest request) {
        System.out.println("controller");
        return roomService.createRoom(postRoomReqDTO, images, request);
    }

    @GetMapping("/rooms")
    public ApiResponse getRoom(@RequestParam(required = false, value="category") String ctgName,
                               @RequestParam(required = false, value="keyword") String keyword,
                               @RequestParam(value="page", required = false) Integer page) {
        int pages = 1;
        if(page!=null){
            pages=page;
        }
        return roomService.getRoomList(ctgName, pages, keyword);
    }

    @PostMapping("/rooms/join/{roomId}")
    public ApiResponse joinRoom(@PathVariable(value ="roomId")  int roomId,  @RequestBody(required = false) JoinRoomDTO joinRoomDTO){
        return roomService.joinRoom(Long.valueOf(roomId), joinRoomDTO);
    }

    // 강제퇴장
    @PostMapping("/rooms/{roomId}/kick/{memberId}")
    public ApiResponse kickMember(@PathVariable(value = "roomId") int roomId, @PathVariable(value = "memberId") int memberId){
        return roomService.kickMember(Long.valueOf(roomId), Long.valueOf(memberId));
    }

    // 룸 퇴장
    @PostMapping("/rooms/{roomId}/leave")
    public ApiResponse outRoom(@PathVariable(value = "roomId") int roomId){
        Long id = Long.valueOf(roomId);
        return roomService.outRoom(id);
    }

    // 방 종료
//    @PostMapping("")

    // 방 수정
    @PostMapping("/rooms/{roomId}/update")
    public ApiResponse updateRooms(@PathVariable(value = "roomId") int roomId, @RequestPart(value ="roomUpdateDTO") RoomUpdateDTO roomUpdateDTO,
                                   @RequestPart(value="images", required = false) MultipartFile images){
        Long id = Long.valueOf(roomId);
        return roomService.updateRoom(id, roomUpdateDTO, images);
    }
}

