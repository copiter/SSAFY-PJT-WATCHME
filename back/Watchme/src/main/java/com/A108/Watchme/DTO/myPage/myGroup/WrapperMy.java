package com.A108.Watchme.DTO.myPage.myGroup;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class WrapperMy {
    private MyGroup myGroup;
    private MyData myData;
}
