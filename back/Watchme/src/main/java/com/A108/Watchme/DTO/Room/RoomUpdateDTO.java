package com.A108.Watchme.DTO.Room;

import com.A108.Watchme.VO.ENUM.CategoryList;
import com.A108.Watchme.VO.ENUM.Status;
import lombok.Getter;
import lombok.Setter;
import org.joda.time.DateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Getter
@Setter
public class RoomUpdateDTO {
    @NotBlank(message = "방 이름을 입력하세요.")
    private String roomName;
    @NotBlank(message = "규칙을 설정하세요.")
    private String mode;
    private Integer pwd;
    private String endAt;
    private String roomDescription;
    @NotNull(message = "인원을 입력하세요.")
    @Positive(message = "인원은 0보다 큰 값이 들어가야 합니다.")
    private Integer roomMemberMaxNo;
    private String roomCategory;
}
