package com.A108.Watchme.DTO;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
public class ResetPwDTO {
    @NotBlank(message = "이메일 키를 입력하세요.")
    String emailKey;
    @NotBlank(message = "비밀번호를 입력하세요.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}",
            message = "비밀번호는 영대소문자, 숫자, 특수문자를 포함한 8~16자 입니다.")
    String password;
}
