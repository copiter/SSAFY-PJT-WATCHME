package com.A108.Watchme.DTO;

import com.A108.Watchme.VO.ENUM.RoomStatus;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RoomCreateDTO {
    private String roomName;
    private RoomStatus status;
    private Integer view;
}
