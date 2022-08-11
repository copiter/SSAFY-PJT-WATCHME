package com.A108.Watchme.Controller;

import com.A108.Watchme.DTO.*;
import com.A108.Watchme.Exception.CustomException;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Http.Code;
import com.A108.Watchme.Repository.MemberRepository;
import com.A108.Watchme.Repository.RefreshTokenRepository;
import com.A108.Watchme.Service.MemberService;
import com.A108.Watchme.Service.S3Uploader;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.utils.CookieUtil;
import io.swagger.annotations.*;
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
import javax.validation.Valid;
import java.text.ParseException;
import java.util.Optional;

@RestController
//@RequestMapping("/members")
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

    @PostMapping(value="/auth/signup", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    public ApiResponse signUp(@Valid @RequestPart(value = "data") SignUpRequestDTO signUpRequestDTO,@RequestPart(value = "files",required = false) MultipartFile images) throws ParseException {
        String url="https://popoimages.s3.ap-northeast-2.amazonaws.com/Watchme/user.png";
        try{
            url = s3Uploader.upload(images, "Watchme");
        } catch (Exception e){
            throw new CustomException(Code.C512);
        }
        return memberService.memberInsert(signUpRequestDTO, url);
    }
    @PostMapping("/login")
    @ResponseBody
    public ApiResponse login(@Valid @RequestBody LoginRequestDTO loginRequestDTO, HttpServletResponse response, HttpServletRequest request){

        ApiResponse apiResponse = memberService.login(request,response, loginRequestDTO);
        return apiResponse;
    }
    @PostMapping("/auth/logout")
    @ResponseBody
    public ApiResponse logout(HttpServletRequest request,
                              HttpServletResponse response){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            Long.parseLong(((UserDetails) authentication.getPrincipal()).getUsername());
        } catch (Exception e){
            throw new CustomException(Code.C501);
        }
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage("LOGOUT SUCCESS");
        apiResponse.setCode(200);

        return apiResponse;
    }

    @PostMapping("/auth/social-signup")
    @ResponseBody
    public ApiResponse socialSignUp(@Valid @RequestBody SocialSignUpRequestDTO socialSignUpRequestDTO, HttpServletRequest request,
                                    HttpServletResponse response, Authentication authentication) throws ParseException {
        return memberService.memberInsert(socialSignUpRequestDTO, request, response ,authentication);
    }

    @PostMapping("/auth/find-email")
    @ResponseBody
    public ApiResponse findEmail(@Valid @RequestBody FindEmailRequestDTO findEmailRequestDTO){
        System.out.println(findEmailRequestDTO.getNickName());
        ApiResponse result = memberService.findEmail(findEmailRequestDTO);
        return result;
    }


    @GetMapping(value = "/members/mygroup")
    @ResponseBody
    public ApiResponse memberGroup() {
        return memberService.memberGroup();
    }

    @PostMapping("/find-pwd")
    public ApiResponse findPW(@Valid @RequestBody FindPwDTO findPwDTO) {
        return memberService.findPW(findPwDTO);
    }

    @PostMapping("/reset-pwd")
    public ApiResponse resetPW(@Valid @RequestBody ResetPwDTO resetPwDTO){
        return memberService.resetPW(resetPwDTO);
    }

    @PostMapping("/change-pwd")
    public ApiResponse resetPwMp(@Valid @RequestBody ResetPwMpDTO resetPwMpDTO){
        return memberService.resetPwMp(resetPwMpDTO);
    }

    @PostMapping(value="/update", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @ResponseBody
    public ApiResponse memberUpdate(@Valid @RequestPart(value = "data") UpdateRequestDTO updateRequestDTO, @RequestPart(value = "files", required = false) MultipartFile image) throws ParseException {
        // 프로필 이미지 수정시 삭제?
        return memberService.memberUpdate(updateRequestDTO, image);
    }

    @PostMapping("/emails-check")
    @ResponseBody
    public ApiResponse emailCheck(@Valid @RequestBody CheckEmailDTO checkEmailDTO){
        return memberService.emailCheck(checkEmailDTO);
    }

    @PostMapping("/nickName-check")
    @ResponseBody
    public ApiResponse nickNameCheck(@Valid @RequestBody CheckNickNameDTO checkNickNameDTO){
        return memberService.nickNameCheck(checkNickNameDTO);
    }

    @GetMapping("/sprints")
    @ResponseBody
    public ApiResponse getMySprints(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long memberId;
        try{
            memberId = Long.parseLong(((UserDetails) authentication.getPrincipal()).getUsername());
        } catch (Exception e){
            throw new CustomException(Code.C501);
        }

        return memberService.getMySprints(memberId);
    }

    @GetMapping("/members")
    @ResponseBody
    public ApiResponse memberInfo(HttpServletResponse response){
        ApiResponse apiResponse;

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication.getPrincipal().equals("anonymousUser")){
            apiResponse = new ApiResponse();
            apiResponse.setCode(400);
            apiResponse.setMessage("NOT LOGIN");
            return apiResponse;
        }

        // 일반 로그인의 경우
        Long currUserId = Long.parseLong(((UserDetails)authentication.getPrincipal()).getUsername());
        Optional<Member> checkCurrUser = memberRepository.findById(currUserId);

        if(checkCurrUser.isPresent()){
            apiResponse = memberService.getMyPage(checkCurrUser.get(), response);
        } else{
            apiResponse = new ApiResponse();
            apiResponse.setCode(400);
            apiResponse.setMessage("NO SUCH USER");
        }

        return apiResponse;

    }

}
