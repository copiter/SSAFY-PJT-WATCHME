package com.A108.Watchme.DTO;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class FindEmailRequestDTO {
    @NotBlank(message = "이름을 입력하세요.")
    @Size(min = 2, max = 7, message = "이름은 2 ~ 5글자 입니다.")
    private String name;
    @NotBlank(message = "닉네임을 입력하세요.")
    @Size(max = 10, message = "닉네임은 10글자 이하입니다.")
    private String nickName;
}
