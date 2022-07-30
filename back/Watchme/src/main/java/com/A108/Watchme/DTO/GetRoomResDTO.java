package com.A108.Watchme.DTO;

import com.A108.Watchme.VO.ENUM.CategoryList;
import com.A108.Watchme.VO.ENUM.RoomStatus;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetRoomResDTO {
    private Long id;
    private String roomName;
    private RoomStatus roomStatus;
    private CategoryList ctgName;
    private int maxNum;
    private int nowNum;
    private Date endTime;
    private String description;
    private String roomImage;
    private boolean secret;
}
