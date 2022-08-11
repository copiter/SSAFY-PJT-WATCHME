package com.A108.Watchme.DTO.KakaoPay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoPayApproveReq {
    private String tid;
    private String pg_token;
    private int value;
}
