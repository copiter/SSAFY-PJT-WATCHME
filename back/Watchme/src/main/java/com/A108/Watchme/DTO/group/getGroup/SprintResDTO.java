package com.A108.Watchme.DTO.group.getGroup;

import lombok.*;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@Getter @Setter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class SprintResDTO {
    private Integer status;
    private String name;
    private String description;
    private String goal;
    private Date startAt;
    private Date endAt;
    private List<String> sprintRuleList;
    private Integer fee;
    private Timestamp routineStartAt;
    private Timestamp routineEndAt;

}
