package com.A108.Watchme.DTO;

import com.A108.Watchme.VO.ENUM.RoomStatus;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.VO.Entity.sprint.Sprint;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter @Setter
public class PostRoomReqDTO {
    private String roomName;
    private String status;
    private Integer roomPwd;
    private String description;
    private String categoryName;
    private Integer num;
    private DateTime endTime;
    private Integer display;}
