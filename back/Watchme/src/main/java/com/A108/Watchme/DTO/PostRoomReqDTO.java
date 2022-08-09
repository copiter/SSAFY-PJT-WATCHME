package com.A108.Watchme.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PostRoomReqDTO {
    private String roomName;
    private String mode;
    private Integer roomPwd;
    private String description;
    private String categoryName;
    private Integer num;
    private String endTime;
    private Integer display;
}
