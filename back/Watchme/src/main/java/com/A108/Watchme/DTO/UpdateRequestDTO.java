package com.A108.Watchme.DTO;

import com.A108.Watchme.VO.ENUM.Gender;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Getter
public class UpdateRequestDTO {
    @NotBlank(message = "이름을 입력해주세요.")
    private String name;
    @NotBlank(message = "닉네임을 입력하세요")
    private String nickName;
    private Gender gender;
    @NotBlank(message = "생년월일을 입력하세요")
    private Date birth;
    private String description;
}