package com.A108.Watchme.DTO.group.getGroupList;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupMemberDetailResDTO {
    private String nickName;
    private String email;
    private String imgLink;
    private Integer studyTime;
    private List<Integer> penalty;
}
