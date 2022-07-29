package com.A108.Watchme.DTO;

import com.A108.Watchme.VO.ENUM.Gender;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter @Setter
@Builder
public class MemberDataDTO {

    private String email;
    private String name;
    private String nickName;
    private Gender gender;
    private Date birth;
    private String profileImage;

    private Integer studyTimeToday;
    private Integer studyTimeWeek;
    private Integer studyTimeMonth;
    private Integer studyTimeTotal;

}
