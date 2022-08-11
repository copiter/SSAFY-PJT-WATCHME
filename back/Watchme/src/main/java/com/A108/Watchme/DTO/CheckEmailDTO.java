package com.A108.Watchme.DTO;

import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
public class CheckEmailDTO {
    @NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "이메일 형식으로 입력하세요.")
    private String email;
}
