package com.A108.Watchme.DTO.group;

import io.swagger.annotations.ApiModel;
import lombok.*;

import java.util.List;

@ApiModel
@Getter @Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupApplyDTO {
    private String email;
    private String nickName;
    private String imgLink;
    private List<Integer> penalty;
    private Integer studyTime;
}
