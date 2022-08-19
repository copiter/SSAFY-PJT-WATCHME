package com.A108.Watchme.DTO.group.getGroup;

import lombok.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class SprintResDTO {
    private Long sprintId;
    private String name;
    private String description;
    private String goal;
    private String mode;
    private String status;
    private String sprintImg;
    private Integer fee;
    private Integer penaltyMoney;
    private String startAt;
    private String endAt;
    private String routineStartAt;
    private String routineEndAt;
    private String kingName;
    private Integer kingPenalty;
    private Integer kingStudy;
    private Integer studySum;
    private Integer penaltySum;
}
