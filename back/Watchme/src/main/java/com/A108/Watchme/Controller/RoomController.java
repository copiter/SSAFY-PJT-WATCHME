package com.A108.Watchme.Controller;

import com.A108.Watchme.DTO.*;
import com.A108.Watchme.DTO.Room.JoinRoomDTO;
import com.A108.Watchme.DTO.Room.PostRoomReqDTO;
import com.A108.Watchme.DTO.Room.RoomUpdateDTO;
import com.A108.Watchme.Exception.CustomException;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Http.Code;
import com.A108.Watchme.Service.RoomService;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RoomController {

    @Autowired
    private RoomService roomService;

    // 룸 생성 API
    @PostMapping(value = "/rooms", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse addRoom(@RequestPart PostRoomReqDTO postRoomReqDTO, @RequestPart(required = false) MultipartFile images, HttpServletRequest request) {
        System.out.println("controller");
        return roomService.createRoom(postRoomReqDTO, images, request);
    }

    // 룸 조회 및 검색 API
    @GetMapping("/rooms")
    public ApiResponse getRoom(@RequestParam(required = false, value="category") String ctgName,
                               @RequestParam(required = false, value="keyword") String keyword,
                               @RequestParam(value="page", required = false) String page){
        System.out.println("page"+ page);
        int pages = 1;
        try{
            if(page!=null){
                pages=Integer.parseInt(page);
            }
        } catch (Exception e){
            throw new CustomException(Code.C521);
        }

        return roomService.getRoomList(ctgName, pages, keyword);
    }

    // 룸 참여 API
    @PostMapping("/rooms/{roomId}/join")
    public ApiResponse joinRoom(@PathVariable(value ="roomId")  int roomId,  @RequestBody(required = false) JoinRoomDTO joinRoomDTO){
        System.out.println(roomId);
        return roomService.joinRoom(Long.valueOf(roomId), joinRoomDTO);
    }

    // 룸 강퇴 API
    @PostMapping("/rooms/{roomId}/kick/{memberId}")
    public ApiResponse kickMember(@PathVariable(value = "roomId") int roomId, @PathVariable(value = "memberId") int memberId){
        return roomService.kickMember(Long.valueOf(roomId), Long.valueOf(memberId));
    }

    // 룸 떠나기 API
    @PostMapping("/rooms/{roomId}/leave")
    public ApiResponse outRoom(@PathVariable(value = "roomId") int roomId){
        Long id = Long.valueOf(roomId);
        return roomService.outRoom(id);
    }

    // 방 종료
//    @PostMapping("")

    // 방 입장 후 최초화면 및 나의 공부 조회
    @GetMapping("/rooms/{roomId}")
    public ApiResponse getRoomDetails(@PathVariable(value = "roomId") int roomId){
        Long id = Long.valueOf(roomId);
        return roomService.getRoomDet(id);
    }

    // 방 입장 후 멤버 목록 조회
    @GetMapping("/rooms/{roomId}/members")
    public ApiResponse getRoomMembers(@PathVariable(value = "roomId") int roomId){
        Long id = Long.valueOf(roomId);
        return roomService.getRoomMem(id);
    }

    // 방 입장 후 방 설정 조회
    @GetMapping("/rooms/{roomId}/settings")
    public ApiResponse getRoomSettings(@PathVariable(value = "roomId") int roomId) {
        Long id = Long.valueOf(roomId);
        return roomService.getRoomSetting(id);
    }

    // 룸 수정 API
    @PostMapping("/rooms/{roomId}/update")
    public ApiResponse updateRooms(@PathVariable(value = "roomId") int roomId, @RequestPart(value ="roomUpdateDTO") RoomUpdateDTO roomUpdateDTO,
                                   @RequestPart(value="images", required = false) MultipartFile images){
        Long id = Long.valueOf(roomId);
        return roomService.updateRoom(id, roomUpdateDTO, images);
    }
}

