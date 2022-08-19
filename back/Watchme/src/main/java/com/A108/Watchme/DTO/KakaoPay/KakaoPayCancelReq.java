package com.A108.Watchme.DTO.KakaoPay;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoPayCancelReq {
    private String cid;
    private String tid;
    private Integer cancel_amount;
    private Integer cancle_tax_free_amount;
}
