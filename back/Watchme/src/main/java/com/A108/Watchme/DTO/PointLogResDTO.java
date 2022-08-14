package com.A108.Watchme.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor @NoArgsConstructor
@Setter
@Builder
public class PointLogResDTO {
    private String date;
    private String content;
    private Integer point;
}
