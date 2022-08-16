package com.A108.Watchme.Controller;


import com.A108.Watchme.DTO.KakaoPay.KakaoPayApproveReq;
import com.A108.Watchme.DTO.KakaoPay.KakaoPayApproveRes;
import com.A108.Watchme.DTO.KakaoPay.KakaoPayRes;
import com.A108.Watchme.Exception.CustomException;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Http.Code;
import com.A108.Watchme.Repository.MemberRepository;
import com.A108.Watchme.Service.PointService;
import com.A108.Watchme.utils.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PointController {
    @Autowired
    private PointService pointService;
    @Autowired
    private AuthUtil authUtil;
    @Autowired
    private MemberRepository memberRepository;
    @PostMapping("/points/kakao")
    public ApiResponse kakaoPayReady(@RequestParam(name = "value") String value){
        int point = 0;
        Long id= authUtil.memberAuth();
        try{
            point = Integer.parseInt(value);
            if(point <= 0){
                throw new CustomException(Code.C598);
            }
        } catch (Exception e){
            throw new CustomException(Code.C521);
        }
        ApiResponse apiResponse = new ApiResponse();
        try{
            KakaoPayRes kakaoPayRes = pointService.kakaoPayReady(id, point);
            apiResponse.setCode(200);
            apiResponse.setMessage("PAY READY SUCCESS");
            apiResponse.setResponseData("REDIRECT_URL", kakaoPayRes.getNext_redirect_pc_url());
            apiResponse.setResponseData("tid", kakaoPayRes.getTid());
        } catch (Exception e){
            throw new CustomException(Code.C597);
        }




        return apiResponse;
    }

    @PostMapping("/points/kakao/approval")
    public ApiResponse kakaoApprove(@RequestBody KakaoPayApproveReq kakaoPayApproveReq){
        ApiResponse apiResponse = new ApiResponse();
        Long id = authUtil.memberAuth();
    try{
        KakaoPayApproveRes kakaoPayRes = pointService.kakaoPayApprove(id, kakaoPayApproveReq);
        pointService.pointSave(kakaoPayApproveReq.getPg_token(), kakaoPayRes.getMoney(), id);
        apiResponse.setCode(200);
        apiResponse.setMessage("PAY APPROVE SUCCESS");
    } catch (Exception e){
        throw new CustomException(Code.C597);
    }
        return apiResponse;
    }

    @PostMapping("/points/return")
    public ApiResponse apiResponse(@RequestParam int value) {
        Long id = authUtil.memberAuth();

        return pointService.pointReturn(id, value);
    }

}


