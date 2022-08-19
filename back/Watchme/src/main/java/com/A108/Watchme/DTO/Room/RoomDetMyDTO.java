package com.A108.Watchme.DTO.Room;

import com.A108.Watchme.VO.ENUM.Mode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class RoomDetMyDTO {
    private String name;
    private String leaderName;
    private Integer leaderTrue;
    private String startTime;
    private Mode mode;
    private Integer penalty;
}
