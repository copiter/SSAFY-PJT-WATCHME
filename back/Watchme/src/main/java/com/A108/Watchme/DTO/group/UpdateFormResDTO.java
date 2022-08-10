package com.A108.Watchme.DTO.group;

import com.A108.Watchme.VO.ENUM.CategoryList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor @NoArgsConstructor
@Builder
public class UpdateFormResDTO {
    private Long id;
    private String name;
    private String description;
    private Integer maxMember;
    private List<String> ctg;
    private String pwd;
    private Integer display;
    private String imgLink;
}
