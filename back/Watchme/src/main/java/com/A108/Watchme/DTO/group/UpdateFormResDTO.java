package com.A108.Watchme.DTO.group;

import com.A108.Watchme.VO.ENUM.CategoryList;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
