package com.A108.Watchme.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class RoomJoinDTO {
    private Long memberId;
    private Long roomId;
}
