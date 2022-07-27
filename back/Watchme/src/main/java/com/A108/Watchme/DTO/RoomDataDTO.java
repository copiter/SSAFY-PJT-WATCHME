package com.A108.Watchme.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class RoomDataDTO {

    private Long ID;
    private String URL;
    private String roomName;
    private String roomImage;
    private String roomDiscription;
    private Integer roomMemberNo;
    private Integer roomMemberMaxNo;
    private Integer roomLookUp;
    private String roomCategory;

}
