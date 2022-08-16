package com.A108.Watchme.DTO;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter
@Setter
@Builder
public class PointLogResDTO {
    private String date;
    private String content;
    private Integer point;
}
