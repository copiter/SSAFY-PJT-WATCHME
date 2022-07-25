package com.A108.Watchme.Service;

import com.A108.Watchme.DTO.LoginRequestDTO;
import com.A108.Watchme.DTO.NewTokenRequestDTO;
import com.A108.Watchme.DTO.SignUpRequestDTO;
import com.A108.Watchme.Exception.AuthenticationException;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Repository.MemberInfoRepository;
import com.A108.Watchme.Repository.MemberRepository;
import com.A108.Watchme.Repository.RefreshTokenRepository;
import com.A108.Watchme.VO.*;
import com.A108.Watchme.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public ApiResponse memberInsert(SignUpRequestDTO signUpRequestDTO) throws ParseException {
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
                .imageLink(signUpRequestDTO.getImageLink())
                .score(0)
                .build());
        result.setMessage("MEMBER INSERT SUCCESS");
        result.setResponseData("DATA", "Success");
        return result;
    }

    public ApiResponse login(LoginRequestDTO loginRequestDTO) {
        ApiResponse result = new ApiResponse();

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
            );

            Member member = memberRepository.findByEmail(loginRequestDTO.getEmail());
            Map createToken = createTokenReturn(member.getId());
            result.setMessage("LOGIN SUCCESS");
            result.setResponseData("accessToken", createToken.get("accessToken"));
            result.setResponseData("refreshToken", createToken.get("refreshToken"));
        } catch (Exception e) {
            e.printStackTrace();
            throw new AuthenticationException(ErrorCode.UsernameOrPasswordNotFoundException);
        }

        return result;
    }

    public ApiResponse newAccessToken(NewTokenRequestDTO newTokenRequestDTO, HttpServletRequest request){
        ApiResponse result = new ApiResponse();
        String refreshToken = newTokenRequestDTO.getToken();

        // AccessToken은 만료되었지만 RefreshToken은 만료되지 않은 경우
        if(jwtProvider.validateJwtToken(request, refreshToken)){
            Long memberId = jwtProvider.getMemberId(refreshToken);

            Map createToken = null;
            try {
                createToken = createTokenReturn(memberId);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            result.setResponseData("accessToken", createToken.get("accessToken"));
            result.setResponseData("refreshToken", createToken.get("refreshToken"));
        }else{
            // RefreshToken 또한 만료된 경우는 로그인을 다시 진행해야 한다.
            result.setResponseData("code", ErrorCode.ReLogin.getCode());
            result.setResponseData("message", ErrorCode.ReLogin.getMessage());
            result.setResponseData("HttpStatus", ErrorCode.ReLogin.getStatus());
        }
        return result;
    }

    private Map<String, String> createTokenReturn(Long memberId) throws ParseException {
        Map result = new HashMap();

        String accessToken = jwtProvider.createAccessToken(memberId);
        String refreshToken = jwtProvider.createRefreshToken(memberId).get("refreshToken");
        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date expiredAt = fm.parse(jwtProvider.createRefreshToken(memberId).get("refreshTokenExpirationAt"));
        RefreshToken insertRefreshToken = RefreshToken.builder()
                .token(refreshToken)
                .expiredAt(expiredAt)
                .build();

        refreshTokenRepository.save(insertRefreshToken);

        result.put("accessToken", accessToken);
        result.put("refreshToken", insertRefreshToken.getToken());
        return result;
    }
}
