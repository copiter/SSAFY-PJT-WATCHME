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
import com.A108.Watchme.utils.CookieUtil;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final static String REFRESH_TOKEN = "refresh_token";
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

        ApiResponse apiResponse = memberService.login(request,response, loginRequestDTO);
        System.out.println(apiResponse.getResponseData());
        return apiResponse;
    }
    @PostMapping("/logout")
    @ResponseBody
    public ApiResponse logout(HttpServletRequest request,
                              HttpServletResponse response, Authentication authentication){
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("LOGOUT SUCCESS");
        apiResponse.setCode(200);

        return apiResponse;
    }

    @PostMapping("/social-signup")
    @ResponseBody
    public ApiResponse socialSignUp(@RequestBody SocialSignUpRequestDTO socialSignUpRequestDTO, HttpServletRequest request,
                                    HttpServletResponse response, Authentication authentication) throws ParseException {
        return memberService.memberInsert(socialSignUpRequestDTO, request, response ,authentication);
    }
}
