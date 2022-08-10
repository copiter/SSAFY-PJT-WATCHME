package com.A108.Watchme.DTO.group.getGroup;

import lombok.*;

import java.util.Date;
import java.util.List;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class GroupResDTO {
    private String name;
    private String description;
    private Integer currMember;
    private Integer maxMember;
    private List<String> ctg;
    private String imgLink;
    private String createAt;
    private Integer display;
    private Integer view;
}
