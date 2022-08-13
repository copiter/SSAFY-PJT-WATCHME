package com.A108.Watchme.DTO;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
public class FindPwDTO {
    @NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "이메일형식이 아닙니다.")
    private String email;

    @NotBlank(message = "이름을 입력하세요.")
    @Size(min = 2, max = 7, message = "이름은 2 ~ 7글자 입니다.")
    private String name;
}
