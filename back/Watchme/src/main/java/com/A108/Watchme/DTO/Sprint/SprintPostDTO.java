package com.A108.Watchme.DTO.Sprint;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;

@Setter
@Getter
public class SprintPostDTO {
    @NotBlank(message = "스프린트 이름을 입력하세요.")
    private String name;
    // TODO : front에서 보내는 request 변수명 확인해볼것
    private String description;
    private String goal;
    private String startAt;
    private String endAt;
    private String routineEndAt;
    private String routineStartAt;
    @NotBlank(message = "규칙을 입력하세요.")
    private String mode;
    @PositiveOrZero(message = "벌금은 0이상으로 입력해주세요.")
    private int penaltyMoney;
    @PositiveOrZero(message = "참가비는 0이상으로 입력해주세요.")
    private int fee;
}
