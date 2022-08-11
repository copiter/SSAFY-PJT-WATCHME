package com.A108.Watchme.DTO.myPage;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WrapperMy {
    private MyGroup myGroup;
    private MyData myData;
}
