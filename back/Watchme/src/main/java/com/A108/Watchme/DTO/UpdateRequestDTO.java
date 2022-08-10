package com.A108.Watchme.DTO;

import com.A108.Watchme.VO.ENUM.Gender;
import lombok.Getter;

import java.util.Date;

@Getter
public class UpdateRequestDTO {
    private String name;
    private String nickName;
    private Gender gender;
    private Date birth;
    private String description;
}