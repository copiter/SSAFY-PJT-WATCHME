package com.A108.Watchme.DTO;

import com.A108.Watchme.VO.ENUM.Gender;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;

@Getter
@Setter
public class SignUpRequestDTO {
    @NotBlank(message = "이메일을 입력하세요.")
    @Email(message = "이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호를 입력하세요.")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,16}",
            message = "비밀번호는 영대소문자, 숫자, 특수문자를 포함한 8~16자 입니다.")
    private String password;

    @NotBlank(message = "이름을 입력하세요.")
    @Size(min = 2, max = 7, message = "이름은 2 ~ 5글자 입니다.")
    private String name;

    @NotBlank(message = "닉네임을 입력하세요.")
    @Size(max = 10, message = "닉네임은 10글자 이하입니다.")
    private String nickName;

    private Gender gender;
    private Date birth;
}
