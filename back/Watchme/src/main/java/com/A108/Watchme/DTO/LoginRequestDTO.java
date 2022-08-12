package com.A108.Watchme.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter @Setter
public class LoginRequestDTO {
    @NotBlank(message = "EMAIL BLANK") @Email(message = "NOT EMAIL")
    private String email;

    @NotBlank(message = "PASSWORD BLANK")
    private String password;
}
