package com.A108.Watchme.Service;

import com.A108.Watchme.Config.properties.AppProperties;
import com.A108.Watchme.DTO.*;
import com.A108.Watchme.Exception.AuthenticationException;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Repository.MemberInfoRepository;
import com.A108.Watchme.Repository.MemberRepository;
import com.A108.Watchme.Repository.RefreshTokenRepository;
import com.A108.Watchme.VO.ENUM.ProviderType;
import com.A108.Watchme.VO.ENUM.Role;
import com.A108.Watchme.VO.ENUM.Status;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.VO.Entity.member.MemberInfo;
import com.A108.Watchme.VO.Entity.RefreshToken;
import com.A108.Watchme.oauth.entity.UserPrincipal;
import com.A108.Watchme.oauth.token.AuthToken;
import com.A108.Watchme.oauth.token.AuthTokenProvider;
import com.A108.Watchme.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.text.ParseException;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final static long THREE_DAYS_MSEC = 259200000;
    private final static String REFRESH_TOKEN = "refresh_token";

    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final S3Uploader s3Uploader;

    @Transactional
    public ApiResponse memberInsert(SignUpRequestDTO signUpRequestDTO, String url) throws ParseException {
        ApiResponse result = new ApiResponse();
        String encPassword = bCryptPasswordEncoder.encode(signUpRequestDTO.getPassword());
        Member member = memberRepository.save(Member.builder()
                .email(signUpRequestDTO.getEmail())
                .nickName(signUpRequestDTO.getNickName())
                .role(Role.MEMBER)
                .pwd(encPassword)
                .status(Status.YES)
                .providerType(ProviderType.EMAIL)
                .build());

        memberInfoRepository.save(MemberInfo.builder()
                .member(member)
                .gender(signUpRequestDTO.getGender())
                .name(signUpRequestDTO.getName())
                .birth(signUpRequestDTO.getBirth())
                .point(0)
                .imageLink(url)
                .score(0)
                .build());

        result.setMessage("MEMBER INSERT SUCCESS");
        result.setResponseData("DATA", "Success");
        return result;
    }

    public ApiResponse login(HttpServletRequest request, HttpServletResponse response, LoginRequestDTO loginRequestDTO) {
        ApiResponse result = new ApiResponse();
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Date now = new Date();
        Member member = memberRepository.findByEmail(loginRequestDTO.getEmail());

        AuthToken accessToken = tokenProvider.createAuthToken(member.getId(),
                ((UserPrincipal) authentication.getPrincipal()).getRoleType().getCode()
                , new Date(now.getTime() + appProperties.getAuth().getTokenExpiry()));

        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

        AuthToken refreshToken = tokenProvider.createAuthToken(
                appProperties.getAuth().getTokenSecret(),
                new Date(now.getTime() + refreshTokenExpiry)
        );

        Optional<RefreshToken> oldRefreshToken = refreshTokenRepository.findByEmail(member.getEmail());

        // 원래 RefreshToken이 있으면 갱신해줘야함
        if(oldRefreshToken.isPresent()){
            RefreshToken  token = refreshTokenRepository.findById(oldRefreshToken.get().getId()).get();
            token.setToken(refreshToken.getToken());
            refreshTokenRepository.save(token);
        }
        // 없으면 생성
        else{
            refreshTokenRepository.save(RefreshToken.builder()
                    .token(refreshToken.getToken())
                    .email(member.getEmail())
                    .build());
        }

        int cookieMaxAge = (int) refreshTokenExpiry / 60;
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);

        result.setCode(200);
        result.setMessage("LOGIN SUCCESS");
        result.setResponseData("accessToken", accessToken.getToken());
        return result;
    }
    public ApiResponse memberInsert(SocialSignUpRequestDTO socialSignUpRequestDTO, HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ParseException {
        ApiResponse result = new ApiResponse();

        String email = ((UserPrincipal) authentication.getPrincipal()).getName();
        Member member= memberRepository.findByEmail(email);

        Date now = new Date();
        memberInfoRepository.save(MemberInfo.builder()
                .member(member)
                .gender(socialSignUpRequestDTO.getGender())
                .name(socialSignUpRequestDTO.getName())
                .birth(socialSignUpRequestDTO.getBirth())
                .point(0)
                .score(0)
                .build());

        AuthToken accessToken = tokenProvider.createAuthToken(member.getId(),
                ((UserPrincipal) authentication.getPrincipal()).getRoleType().getCode()
                , new Date(now.getTime() + appProperties.getAuth().getTokenExpiry()));

        long refreshTokenExpiry = appProperties.getAuth().getRefreshTokenExpiry();

        AuthToken refreshToken = tokenProvider.createAuthToken(
                appProperties.getAuth().getTokenSecret(),
                new Date(now.getTime() + refreshTokenExpiry)
        );

        Optional<RefreshToken> oldRefreshToken = refreshTokenRepository.findByEmail(member.getEmail());

        // 원래 RefreshToken이 있으면 갱신해줘야함
        if(oldRefreshToken.isPresent()){
            RefreshToken  token = refreshTokenRepository.findById(oldRefreshToken.get().getId()).get();
            token.setToken(refreshToken.getToken());
            refreshTokenRepository.save(token);
        }
        // 없으면 생성
        else{
            refreshTokenRepository.save(RefreshToken.builder()
                    .token(refreshToken.getToken())
                    .email(member.getEmail())
                    .build());
        }

        int cookieMaxAge = (int) refreshTokenExpiry / 60;
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN);
        CookieUtil.addCookie(response, REFRESH_TOKEN, refreshToken.getToken(), cookieMaxAge);

        result.setMessage("SOCAIL LOGIN SUCCESS");
        result.setCode(200);
        result.setResponseData("accessToken", accessToken.getToken());
        return result;
    }

    public ApiResponse findEmail(FindEmailRequestDTO findEmailRequestDTO) {
        ApiResponse result = new ApiResponse();
        Member member = memberRepository.findByNickName(findEmailRequestDTO.getNickName());

        if(member == null || !member.getMemberInfo().getName().equals(findEmailRequestDTO.getName())){
            result.setMessage("FIND EMAIL FAIL");
            result.setCode(400);
        }
        else {
            result.setMessage("FIND EMAIL SUCCESS");
            result.setResponseData("email", member.getEmail());
            result.setCode(200);
        }

        return result;

    }

    @Transactional
    public ApiResponse memberUpdate(UpdateRequestDTO updateRequestDTO, MultipartFile image) {
        ApiResponse result = new ApiResponse();
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!authentication.getAuthorities().toString().equals("[ROLE_ANONYMOUS]")) {
                System.out.println("heelo");

                Optional<Member> currUser = memberRepository.findById(Long.parseLong(((UserDetails) (authentication.getPrincipal())).getUsername()));

                if(currUser.isPresent()) {
                    String url = currUser.get().getMemberInfo().getImageLink();
                    try {
                        url = s3Uploader.upload(image, "Watchme");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        currUser.get().setNickName(updateRequestDTO.getNickName());
                        currUser.get().getMemberInfo().setName(updateRequestDTO.getName());
                        currUser.get().getMemberInfo().setBirth(updateRequestDTO.getBirth());
                        currUser.get().getMemberInfo().setGender(updateRequestDTO.getGender());
                        currUser.get().getMemberInfo().setDescription(updateRequestDTO.getDescription());
                        memberRepository.save(currUser.get());
                        memberInfoRepository.save(currUser.get().getMemberInfo());
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                    result.setCode(200);
                    result.setMessage("UPDATE SUCCESS");

                }
            }
        }catch (Exception e){
            result.setCode(400);
            result.setMessage("LOGIN USER ONLY");
        }

        return result;
    }

    public ApiResponse emailCheck(CheckEmailDTO checkEmailDTO) {
        ApiResponse apiResponse = new ApiResponse();

        Member member = memberRepository.findByEmail(checkEmailDTO.getEmail());

        if(member != null){
            apiResponse.setCode(500);
            apiResponse.setMessage("UNAVAILABLE EMAIL");
            return apiResponse;
        }
        apiResponse.setCode(200);
        apiResponse.setMessage("AVAILABLE EMAIL");
        return apiResponse;

    }

    public ApiResponse nickNameCheck(CheckNickNameDTO checkNickNameDTO) {
        ApiResponse apiResponse = new ApiResponse();

        Member member = memberRepository.findByNickName(checkNickNameDTO.getNickName());
        if(member != null) {
            apiResponse.setCode(500);
            apiResponse.setMessage("UNAVAILABLE NiCK NAME");
            return apiResponse;
        }

        apiResponse.setCode(200);
        apiResponse.setMessage("AVAILABLE NiCK NAME");
        return apiResponse;

    }
}
