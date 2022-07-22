package com.A108.Watchme.DTO;

import com.A108.Watchme.VO.Gender;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class SignUpRequestDTO {
    private String email;
    private String password;
    private String name;
    private String nickName;
    private Gender gender;
    private Date birth;
    private String imageLink;
}
