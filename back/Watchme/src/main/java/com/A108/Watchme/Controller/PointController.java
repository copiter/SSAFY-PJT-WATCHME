package com.A108.Watchme.Controller;


import com.A108.Watchme.DTO.KakaoPay.KakaoPayApproveReq;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Service.PointService;
import com.A108.Watchme.oauth.entity.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class PointController {
    private PointService pointService;
    @PostMapping("/points/kakao")
    public ApiResponse kakaoPayReady(@RequestParam int value, Authentication authentication){
        Long id = ((UserPrincipal)(authentication.getPrincipal())).getMemberId();
        pointService.kakaoPayReady(id, value);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(200);
        apiResponse.setMessage("PAY READY SUCCESS");
        return apiResponse;
    }

    @PostMapping("/points/kakao/approval")
    public ApiResponse kakaoApprove(@RequestParam(required = false) String pg_token, @RequestBody KakaoPayApproveReq kakaoPayApproveReq, Authentication authentication){
        Long id = ((UserPrincipal)(authentication.getPrincipal())).getMemberId();
        pointService.kakaoPayApprove(id, pg_token, kakaoPayApproveReq);

        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(200);
        apiResponse.setMessage("PAY READY SUCCESS");
        return apiResponse;
    }
}


