package com.A108.Watchme.DTO.Room;

import com.A108.Watchme.VO.ENUM.CategoryList;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

@Getter
@Setter
public class RoomUpdateDTO {
    private String roomName;
    private Integer pwd;
    private DateTime endAT;
    private String roomDescription;
    private Integer roomMemberMaxNo;
    private String roomCategory;
}
