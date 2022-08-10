package com.A108.Watchme.DTO.Room;

import lombok.*;
import org.checkerframework.checker.index.qual.SearchIndexBottom;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RoomDetSettingDTO {
    private String roomName;
    private String mode;
    private Integer roomPwd;
    private String description;
    private String categoryName;
    private Integer num;
    private String endTime;
    private Integer display;
    private String img;
}
