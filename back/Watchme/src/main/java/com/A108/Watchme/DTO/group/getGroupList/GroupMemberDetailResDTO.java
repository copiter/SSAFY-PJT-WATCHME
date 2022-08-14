package com.A108.Watchme.DTO.group.getGroupList;

import lombok.*;

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
}
