package com.A108.Watchme.DTO.group;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class GroupCreateReqDTO {
    private String name;
    private String description;
    private Integer maxMember;
    private List<String> ctg;
    private String pwd;
    private Integer display;
}
