package com.A108.Watchme.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class GroupDataDTO {

    private Long ID;
    private String groupName;
    private String groupImage;
    private String groupDescription;
    private Integer groupMemberNo;
    private Integer groupMemberMaxNo;
    private Integer groupLookUp;
    private String groupCategory;

}
