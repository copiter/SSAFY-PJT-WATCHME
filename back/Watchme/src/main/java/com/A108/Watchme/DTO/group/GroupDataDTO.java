package com.A108.Watchme.DTO.group;

import com.A108.Watchme.VO.ENUM.CategoryList;
import com.A108.Watchme.VO.Entity.Category;
import com.A108.Watchme.VO.Entity.group.GroupCategory;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
    private List<CategoryList> groupCategory;

}
