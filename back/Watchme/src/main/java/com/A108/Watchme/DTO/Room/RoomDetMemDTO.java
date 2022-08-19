package com.A108.Watchme.DTO.Room;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@AllArgsConstructor
public class RoomDetMemDTO {
    private String images;
    private String nickName;
    private Integer penalty;
}
