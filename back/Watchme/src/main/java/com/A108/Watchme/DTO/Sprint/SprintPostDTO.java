package com.A108.Watchme.DTO.Sprint;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SprintPostDTO {
    private String name;
    private String description;
    private String goal;
    private String startAt;
    private String endAt;
    private String routineEndAt;
    private String routineStartAt;
    private String mode;
    private int penaltyMoney;
    private int fee;
}
