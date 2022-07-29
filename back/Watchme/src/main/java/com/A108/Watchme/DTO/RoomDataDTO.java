package com.A108.Watchme.DTO;

import com.A108.Watchme.VO.ENUM.CategoryList;
import com.A108.Watchme.VO.Entity.Category;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@Builder
public class RoomDataDTO {

    private Long ID;
    private String URL;
    private String roomName;
    private String roomImage;
    private String roomDescription;
    private Integer roomMemberNo;
    private Integer roomMemberMaxNo;
    private Integer roomLookUp;
    private List<CategoryList> roomCategory;

}
