package com.A108.Watchme.DTO.Sprint;

import com.A108.Watchme.VO.ENUM.Mode;
import com.A108.Watchme.VO.ENUM.Status;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SprintGetResDTO {
    private Long sprintId;
    private String sprintName;
    private String description;
    private String goal;
    private Status status;
    private String sprintImg;
    private Mode mode;
    private String endAt;
    private String startAt;
    private String routineStartAt;
    private String routineEndAt;
    private Integer penaltyMoney;
    private String kingName;
    private Integer kingStudy;
    private Integer kingPenalty;
    private Integer studySum;
    private Integer penaltySum;
    private Integer myStudy;
    private Integer myPenalty;
}
