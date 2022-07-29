package com.A108.Watchme.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class RoomLeaveDTO {
    private Long memberId;
    private Long roomId;
    private String roomToken;
}
