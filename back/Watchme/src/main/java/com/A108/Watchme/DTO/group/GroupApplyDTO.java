package com.A108.Watchme.DTO.group;

import io.swagger.annotations.ApiModel;
import lombok.*;

@ApiModel
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupApplyDTO {
    private String email;
    private String nickName;
    private String imgLink;
    private Integer penaltyScore;
    private Integer studyTime;
}
