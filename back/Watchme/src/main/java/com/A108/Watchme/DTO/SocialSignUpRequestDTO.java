package com.A108.Watchme.DTO;

import com.A108.Watchme.VO.ENUM.Gender;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SocialSignUpRequestDTO {
    private String name;
    private String nickName;
    private Gender gender;
    private Date birth;
}
