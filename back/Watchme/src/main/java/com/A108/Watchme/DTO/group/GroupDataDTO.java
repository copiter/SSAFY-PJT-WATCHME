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

    private Long id;
    private String name;
    private String description;
    private Integer currMember;
    private Integer maxMember;
    private String imgLink;

}
