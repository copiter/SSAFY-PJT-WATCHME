package com.A108.Watchme.Controller;

import com.A108.Watchme.DTO.LoginRequestDTO;
import com.A108.Watchme.DTO.NewTokenRequestDTO;
import com.A108.Watchme.DTO.SignUpRequestDTO;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Service.MemberService;
import com.A108.Watchme.VO.MemberInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;

@RestController

public class MemberController {
    @Autowired
    MemberService memberService;

    @PostMapping("/signup")
    @ResponseBody
    public ApiResponse signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) throws ParseException {
        return memberService.memberInsert(signUpRequestDTO);
    }

    @PostMapping("/login")
    @ResponseBody
    public ApiResponse login(@RequestBody @Validated LoginRequestDTO loginRequestDTO){
        return memberService.login(loginRequestDTO);
    }

    @PostMapping("/newtoken")
    @ResponseBody
    public ApiResponse newAccessToken(@RequestBody @Validated NewTokenRequestDTO newTokenRequestDTO, HttpServletRequest request) {
        return memberService.newAccessToken(newTokenRequestDTO, request);
    }
}
