package com.A108.Watchme.Service;

import com.A108.Watchme.DTO.KakaoPay.KakaoPayApproveReq;
import com.A108.Watchme.DTO.KakaoPay.KakaoPayApproveRes;
import com.A108.Watchme.DTO.KakaoPay.KakaoPayReq;
import com.A108.Watchme.DTO.KakaoPay.KakaoPayRes;
import com.A108.Watchme.Exception.CustomException;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Http.Code;
import com.A108.Watchme.Repository.MemberInfoRepository;
import com.A108.Watchme.Repository.MemberRepository;
import com.A108.Watchme.Repository.PointLogRepository;
import com.A108.Watchme.VO.Entity.log.PointLog;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.VO.Entity.member.MemberInfo;
import com.A108.Watchme.oauth.entity.UserPrincipal;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PointService {
    private final PointLogRepository pointLogRepository;
    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;
    public KakaoPayRes kakaoPayReady(Long id, Integer point) {
        System.out.println("hello");
        KakaoPayReq kakaoPayReq = KakaoPayReq.builder()
                .cid("TC0ONETIME")
                .partner_order_id("포인트 결제")
                .partner_user_id(Long.toString(id))
                .item_name("WATCH ME POINT 결제")
                .quantity(1)
                .total_amount(point)
                .tax_free_amount(0)
                .approval_url("https://watchme1.shop/PointSuccess")
                .fail_url("https://watchme1.shop/PointFail")
                .cancel_url("https://watchme1.shop/PointCancel")
                .build();

        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK f40f549f7b42eada530093aefb9689ab");
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", kakaoPayReq.getCid());
        params.add("partner_order_id", kakaoPayReq.getPartner_order_id());
        params.add("partner_user_id", kakaoPayReq.getPartner_user_id());
        params.add("item_name", kakaoPayReq.getItem_name());
        params.add("quantity", Integer.toString(kakaoPayReq.getQuantity()));
        params.add("total_amount", Integer.toString(kakaoPayReq.getTotal_amount()));
        params.add("tax_free_amount", Integer.toString(kakaoPayReq.getTax_free_amount()));
        params.add("approval_url", kakaoPayReq.getApproval_url());
        params.add("cancel_url", kakaoPayReq.getCancel_url());
        params.add("fail_url", kakaoPayReq.getFail_url());

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v1/payment/ready",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );
        KakaoPayRes kakaoPayRes = new KakaoPayRes();
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            kakaoPayRes = objectMapper.readValue(response.getBody(), KakaoPayRes.class);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return kakaoPayRes;
    }
    public KakaoPayApproveRes kakaoPayApprove(Long id, KakaoPayApproveReq kakaoPayApproveReq){
        RestTemplate rt = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK f40f549f7b42eada530093aefb9689ab");
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("cid", "TC0ONETIME");
        params.add("tid", kakaoPayApproveReq.getTid());
        params.add("partner_order_id", "포인트 결제");
        params.add("partner_user_id", Long.toString(id));
        params.add("pg_token", kakaoPayApproveReq.getPg_token());

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v1/payment/approve",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class
        );

        KakaoPayApproveRes kakaoPayApproveRes = new KakaoPayApproveRes();
        ObjectMapper objectMapper = new ObjectMapper();
        try{
            kakaoPayApproveRes = objectMapper.readValue(response.getBody(), KakaoPayApproveRes.class);
        } catch (JsonMappingException exception) {
            exception.printStackTrace();
        } catch (JsonProcessingException exception) {
            exception.printStackTrace();
        }
        return kakaoPayApproveRes;
    }

    public void pointSave(String pg_token, int value, Long id) {
        Optional<Member> member = memberRepository.findById(id);
        member.get().getMemberInfo().setPoint(member.get().getMemberInfo().getPoint()+value);
        pointLogRepository.save(PointLog.builder()
                .member(member.get())
                .createdAt(new Date())
                .pointValue(value)
                .pgToken(pg_token)
                .build());

    }

    public ApiResponse pointReturn(Long id, int value) {
        ApiResponse apiResponse = new ApiResponse();
        Member member = memberRepository.findById(id).get();
        int memberPoint = member.getMemberInfo().getPoint();
        if(memberPoint < value){
            throw new CustomException(Code.C538);
        }

        member.getMemberInfo().setPoint(memberPoint-value);
        pointLogRepository.save(PointLog.builder()
                .member(member)
                .createdAt(new Date())
                .pointValue(-value)
                .build());
        apiResponse.setCode(200);
        apiResponse.setMessage("Return Success");

        return apiResponse;
    }
}
