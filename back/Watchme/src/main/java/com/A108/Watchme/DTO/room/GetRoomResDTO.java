package com.A108.Watchme.DTO.Room;

import com.A108.Watchme.VO.ENUM.CategoryList;
import com.A108.Watchme.VO.ENUM.Mode;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetRoomResDTO {
    @Schema(description = "방 식별자 ID")
    private Long id;
    @Schema(description = "방 이름")
    private String roomName;
    @Schema(description = "방 모드", example = "TAG1, TAG2, TAG3")
    private Mode mode;
    @Schema(description = "방 카테고리 이름")
    private CategoryList ctgName;
    @Schema(description = "방 최대 인원")
    private int maxNum;
    @Schema(description = "방 현재 인원")
    private int nowNum;
    @DateTimeFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    @Schema(description = "방 종료 시간")
    private String endTime;
    @Schema(description = "방 이름")
    private String description;
    @Schema(description = "방 이미지 링크")
    private String roomImage;
    @Schema(description = "비밀번호 여부")
    private boolean secret;
}
