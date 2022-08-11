package com.A108.Watchme.Service;

import com.A108.Watchme.Config.properties.AppProperties;
import com.A108.Watchme.DTO.*;
import com.A108.Watchme.Exception.AuthenticationException;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Repository.MemberEmailKeyRepository;
import com.A108.Watchme.Repository.MemberInfoRepository;
import com.A108.Watchme.Repository.MemberRepository;
import com.A108.Watchme.Repository.RefreshTokenRepository;
import com.A108.Watchme.VO.ENUM.ProviderType;
import com.A108.Watchme.VO.ENUM.Role;
import com.A108.Watchme.VO.ENUM.Status;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.VO.Entity.member.MemberEmailKey;
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
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MemberService {
    private final static long THREE_DAYS_MSEC = 259200000;
    private final static String REFRESH_TOKEN = "refresh_token";

    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberEmailKeyRepository memberEmailKeyRepository;
    private final AuthenticationManager authenticationManager;
    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final S3Uploader s3Uploader;
    private final MailService mailService;


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

    public ApiResponse findPW(FindPwDTO resetPwDTO) {
        ApiResponse result = new ApiResponse();

        Member member = memberRepository.findByEmail(resetPwDTO.getEmail());

        if(member == null){
            result.setCode(400);
            result.setMessage("이메일 입력이 잘못 되었습니다.");
            return result;
        }
        if(!resetPwDTO.getName().equals(member.getMemberInfo().getName())){
            result.setCode(400);
            result.setMessage("이름 입력이 잘못 되었습니다.");
            return result;
        }

        String uuid = UUID.randomUUID().toString();

        memberEmailKeyRepository.save(MemberEmailKey.builder()
                .member(member)
                .emailKey((uuid))
                .createdAt(new Date())
                .build());


        // localhost:81 바꿔주기;
        String email = member.getEmail();
        String subject = "Watchme 사이트 비밀번호 초기화 메일입니다. ";
        String text = "<p>Watchme 사이트 비밀번호를 초기화 합니다. </p><p>아래 링크를 클릭하셔서 비밀번호 초기화를 완료하세요.</p>"
                + "<form action='http://localhost:3000/changePWD' method='GET'>"
                + "<input type='hidden' name='emailKey' value= '"+ uuid+ "'/> "
                + "<input type='submit' value='비밀번호 초기화'/>"
                + "</form></div>";
        mailService.sendMail(email, subject, text);
        result.setMessage("EMAIL SEND SUCCESS");
        return result;
    }

    public ApiResponse resetPW(ResetPwDTO resetPwDTO)  {
        ApiResponse result = new ApiResponse();
        System.out.println(resetPwDTO.getEmailKey());
        MemberEmailKey memberEmailKey =  memberEmailKeyRepository.findByEmailKey(resetPwDTO.getEmailKey());

        if(memberEmailKey != null){

            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, - 1);

            System.out.println(memberEmailKey);

            if(memberEmailKey.getCreatedAt().compareTo(  cal.getTime() ) < 0){
                // 유효 기간을 넘겼다.
                // 삭제 : 이메일 초기화 키

                // 시간 제한을 넘긴 인증 코드 삭제
                memberEmailKeyRepository.deleteById(memberEmailKey.getId());

                result.setMessage("EMAIL CODE HAS EXPIRED");
                result.setCode(400);

                return result;
            }

            Member member = memberRepository.findById(memberEmailKey.getId()).get();
            if(member != null){
                String encPassword = bCryptPasswordEncoder.encode(resetPwDTO.getPassword());
                member.setPwd(encPassword);

                memberEmailKeyRepository.deleteById(member.getId());

                result.setMessage("PASSWORD RESET SUCCESS");
                result.setCode(200);
                return result;
            }

            result.setMessage("PASSWORD RESET FAIL");
            result.setCode(400);
            return result;

        }

        else{
            result.setMessage("EMAIL CODE ALREADY USED");
            result.setCode(400);

            return result;
        }
    }

    public ApiResponse resetPwMp(ResetPwMpDTO resetPwMpDTO) {
        ApiResponse result = new ApiResponse();

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!authentication.getAuthorities().toString().equals("[ROLE_ANONYMOUS]")) {

                UserDetails currUser = (UserDetails) authentication.getPrincipal();
                Member mem = memberRepository.findByEmail(currUser.getUsername());
                Member member = memberRepository.findById(mem.getId()).get();

                if(bCryptPasswordEncoder.matches(resetPwMpDTO.getPassword(), member.getPwd())){
                    System.out.println(resetPwMpDTO.getPassword());
                    System.out.println(resetPwMpDTO.getNewPassword());

                    String encPassword = bCryptPasswordEncoder.encode(resetPwMpDTO.getNewPassword());
                    System.out.println(member.getPwd());
                    System.out.println(encPassword);

                    member.setPwd(encPassword);

                    result.setMessage("RESET PASSWORD SUCCESS");
                    result.setCode(200);

                    System.out.println(member.getPwd());
                    System.out.println(encPassword);
                }
                else {
                    result.setMessage("PASSWORD IS NOT CORRECT");
                    result.setCode(400);
                }
            }
            else {
                result.setMessage("NOT LOGGINED");
                result.setCode(400);
            }

        }catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
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
