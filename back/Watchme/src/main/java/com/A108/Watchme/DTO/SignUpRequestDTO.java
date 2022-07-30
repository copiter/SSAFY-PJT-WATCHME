package com.A108.Watchme.DTO;

import com.A108.Watchme.VO.ENUM.Gender;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@Getter
@Setter
public class SignUpRequestDTO {
    @ApiModelProperty(example = "test123@naver.com", required = true)
    private String email;
    @ApiModelProperty(example = "test123!@#", required = true)
    private String password;
    @ApiModelProperty(example = "홍석인", required = true)
    private String name;
    @ApiModelProperty(example = "홍석인짱123", required = true)
    private String nickName;
    @ApiModelProperty(example = "M", required = true)
    private Gender gender;
    @ApiModelProperty(example = "1999:02:03", required = true)
    private Date birth;
}
