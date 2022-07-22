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

@RestController

public class MemberController {
    @Autowired
    MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        memberService.memberInsert(signUpRequestDTO);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/login")
    @ResponseBody
    public ApiResponse login(@RequestBody @Validated LoginRequestDTO loginRequestDTO){
        System.out.println("hi");
        return memberService.login(loginRequestDTO);
    }

    @PostMapping("/newtoken")
    @ResponseBody
    public ApiResponse newAccessToken(@RequestBody @Validated NewTokenRequestDTO newTokenRequestDTO, HttpServletRequest request) {
        return memberService.newAccessToken(newTokenRequestDTO, request);
    }
}
