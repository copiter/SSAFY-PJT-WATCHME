package com.A108.Watchme.DTO.myPage;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyGroup {
    private Long id;
    private String name;
    private String description;
    private List<String> ctg;
    private String imgLink;
}
