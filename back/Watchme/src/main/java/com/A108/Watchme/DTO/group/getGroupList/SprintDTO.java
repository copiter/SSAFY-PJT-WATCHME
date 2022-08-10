package com.A108.Watchme.DTO.group.getGroupList;

import lombok.*;

import java.util.Date;

@Getter @Setter
@Builder
@AllArgsConstructor @NoArgsConstructor
public class SprintDTO {

    private String name;
    private String description;
    private Date startAt;
    private Date endAt;

}
