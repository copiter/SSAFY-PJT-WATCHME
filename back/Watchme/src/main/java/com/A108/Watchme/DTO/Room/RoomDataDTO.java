package com.A108.Watchme.DTO.Room;

import com.A108.Watchme.VO.ENUM.CategoryList;
import com.A108.Watchme.VO.Entity.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@Builder
public class RoomDataDTO {

    private Long id;
    private String roomName;
    private String roomStatus;
    private String ctgName;
    private Integer maxNum;
    private Integer nowNum;
    private String endTime;
    private String description;
    private String roomImage;
    private boolean secret;

}
