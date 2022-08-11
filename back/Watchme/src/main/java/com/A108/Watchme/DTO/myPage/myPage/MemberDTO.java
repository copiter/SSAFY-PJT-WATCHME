package com.A108.Watchme.DTO.myPage.myPage;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDTO {
    private String nickName;
    private String profileImage;
    private int point;
    private String description;
    private int studyTimeToday;
    private int studyTimeWeek;
    private int studyTimeMonth;
    private int studyTimeTotal;
}
