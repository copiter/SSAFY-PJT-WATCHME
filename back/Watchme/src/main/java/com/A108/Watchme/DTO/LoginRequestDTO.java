package com.A108.Watchme.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter @Setter
public class LoginRequestDTO {
    @NotBlank(message = "이메일이 없습니다.") @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호가 없습니다.")
    private String password;
}
