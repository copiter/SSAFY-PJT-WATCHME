package com.A108.Watchme.Controller;

import com.A108.Watchme.DTO.LoginRequestDTO;
import com.A108.Watchme.DTO.NewTokenRequestDTO;
import com.A108.Watchme.DTO.SignUpRequestDTO;
import com.A108.Watchme.DTO.SocialSignUpRequestDTO;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Service.MemberService;
import com.A108.Watchme.Service.S3Uploader;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.ParseException;

@RestController
public class MemberController {
    @Autowired
    MemberService memberService;
    @Autowired
    private S3Uploader s3Uploader;
    @ApiOperation(value="회원가입", notes="성공시 200코드를 반환합니다.")
    @PostMapping(value="/signup", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    public ApiResponse signUp(@RequestPart(value = "request") SignUpRequestDTO signUpRequestDTO, @RequestPart(value = "images") MultipartFile images) throws ParseException {
        String url="https://popoimages.s3.ap-northeast-2.amazonaws.com/Watchme/user.png";
        try{
            url = s3Uploader.upload(images, "Watchme");
        } catch (Exception e){
            e.printStackTrace();
        }
        return memberService.memberInsert(signUpRequestDTO, url);
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
    public ApiResponse socialSignUp(@RequestBody SocialSignUpRequestDTO socialSignUpRequestDTO, HttpServletRequest request,
                                    HttpServletResponse response, @CookieValue(value = "JSESSIONID", required = true) Cookie authCookie) throws ParseException {

        HttpSession httpSession = request.getSession(false);

        if(httpSession!=null){
            ApiResponse apiResponse = memberService.memberInsert(socialSignUpRequestDTO, httpSession);
            String token = apiResponse.getResponseData().get("refreshToken").toString();
            Cookie cookie = new Cookie("refreshToken", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            response.addCookie(cookie);
            apiResponse.getResponseData().remove("refreshToken");
            return apiResponse;
        }
        else{
            throw new RuntimeException("잘못된 접근");
        }
    }
}
