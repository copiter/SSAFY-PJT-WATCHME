package com.A108.Watchme.DTO.myPage.myGroup;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyData {
    private Integer studyTime;
    private List<Integer> penalty;
    private String joinDate;
}
