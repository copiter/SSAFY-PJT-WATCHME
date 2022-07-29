package com.A108.Watchme.DTO;

import com.A108.Watchme.VO.ENUM.RoomStatus;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.VO.Entity.sprint.Sprint;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RoomCreateDTO {
    private String roomName;
    private RoomStatus status;
    private Integer view;
    private Integer roomPwd;
    private Member member;
    private Sprint sprint;
    private Integer num;
    private Integer display;
    private String imageLink;

}
