package com.A108.Watchme.DTO.group;

import lombok.Getter;

import java.util.List;

@Getter
public class GroupUpdateReqDTO {
    private String name;
    private String description;
    private String maxMember;
    private List<String> ctg;
    private Integer display;
}
