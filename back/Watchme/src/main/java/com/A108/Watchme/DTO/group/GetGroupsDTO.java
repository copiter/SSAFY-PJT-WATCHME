package com.A108.Watchme.DTO.group;

import com.A108.Watchme.DTO.group.getGroupList.SprintDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
@Builder
public class GetGroupsDTO {
    private Long id;
    private String name;
    private String description;
    private Integer currMember;
    private Integer maxMember;
    private List<String> ctg;
    private String createAt;
    private String imgLink;
    private boolean secret;
    private int view;
    private SprintDTO sprint;
}
