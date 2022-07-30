package com.A108.Watchme.Controller;

import com.A108.Watchme.DTO.*;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Service.RoomService;
import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
public class RoomController {

    @Autowired
    private RoomService roomService;

    @ApiOperation(value="룸 생성", notes="룸 생성 성공시 200코드를 반환합니다.")
    @PostMapping(value = "/addRoom", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse addRoom(@RequestPart PostRoomReqDTO postRoomReqDTO, @RequestPart MultipartFile images) {
        return roomService.createRoom(postRoomReqDTO, images);
    }

    @ApiOperation(value="룸 조회", notes="룸 조회 성공시 룸정보들을 반환합니다.")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "category", value="카테고리 이름", paramType = "query"),
            @ApiImplicitParam(name = "page", value ="갖고올 페이지", required = true, paramType = "query", defaultValue = "1")
    })
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "GETROOMS SUCCESS", content=@Content(schema = @Schema(implementation = GetRoomResDTO.class)))
    @GetMapping("/room/recruit")
    public ApiResponse getRoom(@RequestParam(required = false, value="category") String ctgName, @RequestParam(value="page", required = false) Integer page, HttpServletRequest request) {
        int pages = 1;
        if(page!=null){
            pages=page;
        }
        return roomService.getRoom(ctgName, pages);
    }

    @ApiOperation(value="룸 참여", notes="룸 참여 성공시 200코드를 반환합니다.")
    @PostMapping("/room/join/{roomId}")
    @ApiImplicitParam(name="roomId", value="룸ID", required = true)
    public ApiResponse joinRoom(@PathVariable(value ="roomId")  int roomId){
        return roomService.joinRoom(Long.valueOf(roomId));
    }

}

