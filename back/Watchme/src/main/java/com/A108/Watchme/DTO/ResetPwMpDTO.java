package com.A108.Watchme.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter @Setter
public class ResetPwMpDTO {
    @NotBlank(message = "기존 비밀번호를 입력하세요.")
    String password;
    @NotBlank(message = "새로운 비밀번호를 입력하세요.")
    @Pattern(regexp = "[a-zA-Z1-9]{6,12}", message = "비밀번호는 영어와 숫자를 포함한 6자이상 12자 이하입니다.")
    String newPassword;
}
