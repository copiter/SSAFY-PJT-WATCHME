package com.A108.Watchme.Controller;

import com.A108.Watchme.DTO.LoginRequestDTO;
import com.A108.Watchme.DTO.NewTokenRequestDTO;
import com.A108.Watchme.DTO.SignUpRequestDTO;
import com.A108.Watchme.DTO.SocialSignUpRequestDTO;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Repository.MemberRepository;
import com.A108.Watchme.Repository.RefreshTokenRepository;
import com.A108.Watchme.Service.MemberService;
import com.A108.Watchme.Service.S3Uploader;
import com.A108.Watchme.VO.Entity.RefreshToken;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.auth.AuthDetails;
import io.swagger.annotations.*;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    @Autowired
    private S3Uploader s3Uploader;

    @ApiOperation(value="회원가입", notes="성공시 200코드를 반환합니다.")
//    @ApiImplicitParams({
//            @ApiImplicitParam(name="email", value="유저 EMAIL"),
//            @ApiImplicitParam(name="password", value="유저 비밀번호")
//    })
    @PostMapping(value="/signup", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    public ApiResponse signUp(@ApiParam @RequestPart(value = "data") SignUpRequestDTO signUpRequestDTO, @ApiParam @RequestPart(value = "files") MultipartFile images) throws ParseException {
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
    @PostMapping("/logout")
    @ResponseBody
    public ApiResponse logout(HttpServletRequest request,
                              HttpServletResponse response, @CookieValue(value = "JSESSIONID", required = false) Cookie authCookie,
                              @CookieValue(value="refreshToken", required = false) Cookie cookie){

        ApiResponse apiResponse = new ApiResponse();
        HttpSession httpSession = request.getSession(false);
        // 소셜로그인인 경우
        if(httpSession!= null){
            httpSession.invalidate();
        }
        // 일반 로그인의 경우
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if(!authentication.getAuthorities().toString().equals("[ROLE_ANONYMOUS]")) {
                refreshTokenRepository.deleteAllByToken(cookie.getValue());
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        apiResponse.setCode(200);
        apiResponse.setMessage("LOGOUT SUCCESS");

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
                                    HttpServletResponse response, @CookieValue(value = "JSESSIONID", required = false) Cookie authCookie, Authentication authentication) throws ParseException {
        AuthDetails authDetails = (AuthDetails) authentication.getPrincipal();
        System.out.println(authDetails.getAttributes());
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
