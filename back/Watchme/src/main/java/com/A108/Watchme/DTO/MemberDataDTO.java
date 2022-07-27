package com.A108.Watchme.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@Builder
public class MemberDataDTO {

    private String email;
    private String username;
    private String nickname;
    private String sex;
    private String birthday;
    private String profileImage;
    private String studyTimeToday;
    private String studyTimeWeek;
    private String studyTimeMonth;
    private String studyTimeTotal;
    private String studyPanaltyTotay;
    private String studyPanaltyWeek;
    private String studyPanaltyMonth;
    private String studyPanaltyTotal;

}
