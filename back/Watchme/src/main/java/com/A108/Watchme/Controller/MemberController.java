package com.A108.Watchme.Controller;

import com.A108.Watchme.DTO.LoginRequestDTO;
import com.A108.Watchme.DTO.NewTokenRequestDTO;
import com.A108.Watchme.DTO.SignUpRequestDTO;
import com.A108.Watchme.DTO.SocialSignUpRequestDTO;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Service.MemberService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;

@RestController
public class MemberController {
    @Autowired
    MemberService memberService;

    private HttpSession httpSession;
    @PostMapping("/signup")
    @ResponseBody
    public ApiResponse signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) throws ParseException {
        return memberService.memberInsert(signUpRequestDTO);
    }
    @PostMapping("/login")
    @ResponseBody
    public ApiResponse login(@RequestBody @Validated LoginRequestDTO loginRequestDTO, HttpServletResponse response, HttpServletRequest request){

        ApiResponse apiResponse = memberService.login(loginRequestDTO);
        String token = apiResponse.getResponseData().get("refreshToken").toString();
        Cookie cookie = new Cookie("refreshToken", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        response.addCookie(cookie);
        apiResponse.getResponseData().remove("refreshToken");
        return apiResponse;
    }

    @PostMapping("/newtoken")
    @ResponseBody
    public ApiResponse newAccessToken(@RequestBody @Validated NewTokenRequestDTO newTokenRequestDTO, HttpServletRequest request) {
        return memberService.newAccessToken(newTokenRequestDTO, request);
    }

    @PostMapping("/social-signup")
    @ResponseBody
    public ApiResponse socialSignUp(@RequestBody SocialSignUpRequestDTO socialSignUpRequestDTO, HttpServletRequest request, HttpServletResponse response){

        httpSession=request.getSession(false);
        if(httpSession!=null){
            return memberService.memberInsert(socialSignUpRequestDTO, httpSession);
        }
        else{
            throw new RuntimeException("잘못된 접근");
        }
    }
}
