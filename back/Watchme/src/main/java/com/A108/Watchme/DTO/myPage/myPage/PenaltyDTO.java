package com.A108.Watchme.DTO.myPage.myPage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor @NoArgsConstructor
@Builder
public class PenaltyDTO {
    private String ruleName;
    private String createdAt;
    private int score;
}
