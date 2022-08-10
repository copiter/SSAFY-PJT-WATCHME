package com.A108.Watchme.DTO.group.getGroupList;

import com.A108.Watchme.DTO.SprintDTO;
import lombok.*;

import java.util.Date;
import java.util.List;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class GroupListResDTO {

    public Long id;
    private String name;
    private String description;
    private Integer currMember;
    private Integer maxMember;
    private List<String> ctg;
    private String imgLink;
    private Date createdAt;
    private Integer display;
    private Integer view;
    private SprintDTO sprint;

}
