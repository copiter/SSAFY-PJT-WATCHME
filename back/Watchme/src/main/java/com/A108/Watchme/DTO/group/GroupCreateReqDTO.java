package com.A108.Watchme.DTO.group;

import lombok.Builder;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
public class GroupCreateReqDTO {
    @NotBlank(message = "그룹명을 입력하세요.")
    @Size(min = 2, max = 7, message = "그룹명은 2 ~ 7글자 입니다.")
    private String name;
    @NotBlank(message = "그룹 설명을 입력하세요")
    @Size(min = 5, max = 50, message = "5 ~ 50자 입력가능합니다.")
    private String description;
    @Positive(message = "양수를 입력하세요.")
    private Integer maxMember;
    @NotNull
    private List<String> ctg;
    //TODO : 그룹에서 pwd 삭제. secret은 받을건지??
    private Integer secret;
}
