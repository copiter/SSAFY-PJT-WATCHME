package com.A108.Watchme.DTO.Room;

import com.A108.Watchme.VO.ENUM.CategoryList;
import com.A108.Watchme.VO.ENUM.Mode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetRoomResDTO {

    private Long id;
    private String roomName;
    private Mode mode;
    private CategoryList ctgName;
    private int maxNum;
    private int nowNum;
    private String endTime;
    private String description;
    private String roomImage;
    private boolean secret;
}
