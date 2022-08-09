package com.A108.Watchme.DTO.Room;

import com.A108.Watchme.VO.ENUM.CategoryList;
import com.A108.Watchme.VO.ENUM.Status;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

@Getter
@Setter
public class RoomUpdateDTO {
    private String roomName;
    private String status;
    private Integer pwd;
    private String endAt;
    private String roomDescription;
    private Integer roomMemberMaxNo;
    private String roomCategory;
    private int display;
}
