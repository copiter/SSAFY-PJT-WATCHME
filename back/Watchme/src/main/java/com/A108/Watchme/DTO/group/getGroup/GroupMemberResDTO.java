package com.A108.Watchme.DTO.group.getGroup;

import lombok.*;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class GroupMemberResDTO {
    private String nickName;
    private String imgLink;
    private Integer role;
}
