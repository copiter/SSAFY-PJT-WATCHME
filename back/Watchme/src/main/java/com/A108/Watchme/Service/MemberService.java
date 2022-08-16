package com.A108.Watchme.Service;

import com.A108.Watchme.Config.properties.AppProperties;
import com.A108.Watchme.VO.Entity.log.PointLog;
import com.A108.Watchme.utils.AuthUtil;
import com.A108.Watchme.DTO.*;
import com.A108.Watchme.DTO.Sprint.SprintGetResDTO;
import com.A108.Watchme.DTO.myPage.myPage.MemberDTO;
import com.A108.Watchme.DTO.myPage.myGroup.MyData;
import com.A108.Watchme.DTO.myPage.myGroup.MyGroup;
import com.A108.Watchme.DTO.myPage.myGroup.WrapperMy;

import com.A108.Watchme.Exception.CustomException;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Http.Code;
import com.A108.Watchme.Repository.*;
import com.A108.Watchme.VO.ENUM.*;
import com.A108.Watchme.VO.Entity.MemberGroup;
import com.A108.Watchme.VO.Entity.group.Group;
import com.A108.Watchme.VO.Entity.log.MemberRoomLog;
import com.A108.Watchme.Repository.MemberEmailKeyRepository;
import com.A108.Watchme.Repository.MemberInfoRepository;
import com.A108.Watchme.Repository.MemberRepository;
import com.A108.Watchme.Repository.RefreshTokenRepository;
import com.A108.Watchme.VO.ENUM.ProviderType;
import com.A108.Watchme.VO.ENUM.Role;
import com.A108.Watchme.VO.ENUM.Status;
import com.A108.Watchme.VO.Entity.log.PenaltyLog;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.VO.Entity.member.MemberEmailKey;
import com.A108.Watchme.VO.Entity.member.MemberInfo;
import com.A108.Watchme.VO.Entity.RefreshToken;
import com.A108.Watchme.VO.Entity.sprint.Sprint;
import com.A108.Watchme.oauth.entity.UserPrincipal;
import com.A108.Watchme.oauth.token.AuthToken;
import com.A108.Watchme.oauth.token.AuthTokenProvider;
import com.A108.Watchme.utils.CookieUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class MemberService {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat format3 = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 mm분");
    SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
    private final static long THREE_DAYS_MSEC = 259200000;
    private final static String REFRESH_TOKEN = "refresh_token";
    private AuthUtil authUtil;
    private final MemberRepository memberRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final MRLRepository mrlRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final MemberEmailKeyRepository memberEmailKeyRepository;
    private final AuthenticationManager authenticationManager;
    private final AppProperties appProperties;
    private final AuthTokenProvider tokenProvider;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final S3Uploader s3Uploader;
    private final MailService mailService;
    private final PenaltyLogRegistory penaltyLogRegistory;
    private final SprintRepository sprintRepository;
    private final PointLogRepository pointLogRepository;
    private final GroupApplyLogRegistory groupApplyLogRegistory;

    @Transactional
    public ApiResponse memberInsert(SignUpRequestDTO signUpRequestDTO, String url) throws ParseException {
        ApiResponse result = new ApiResponse();

        if(emailCheckFunc(signUpRequestDTO.getEmail())){
            throw new CustomException(Code.C514);
        }

        if(nickNameCheckFunc(signUpRequestDTO.getNickName())){
            throw new CustomException(Code.C515);
        }

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
                .studyTime(0)
                .studyTimeDay(0)
                .studyTimeMonth(0)
                .studyTimeWeek(0)
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
        if (oldRefreshToken.isPresent()) {
            RefreshToken token = refreshTokenRepository.findById(oldRefreshToken.get().getId()).get();
            token.setToken(refreshToken.getToken());
            refreshTokenRepository.save(token);
        }
        // 없으면 생성
        else {
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

    public ApiResponse memberInsert(SocialSignUpRequestDTO socialSignUpRequestDTO, HttpServletRequest request,
                                    HttpServletResponse response) throws ParseException {
        ApiResponse result = new ApiResponse();
        if(nickNameCheckFunc(socialSignUpRequestDTO.getNickName())){
            throw new CustomException(Code.C515);
        }
        Member member = memberRepository.findById(authUtil.memberAuth()).get();
        System.out.println("memberId");
        System.out.println(member.getId());
        memberInfoRepository.save(MemberInfo.builder()
                .member(member)
                .gender(socialSignUpRequestDTO.getGender())
                .name(socialSignUpRequestDTO.getName())
                .birth(socialSignUpRequestDTO.getBirth())
                .point(0)
                .score(0)
                .build());


        result.setMessage("SOCAIL LOGIN SUCCESS");
        result.setCode(200);
        return result;
    }

    public ApiResponse findEmail(FindEmailRequestDTO findEmailRequestDTO) {
        ApiResponse result = new ApiResponse();
        Member member = memberRepository.findByNickName(findEmailRequestDTO.getNickName());

        if (member == null || !member.getMemberInfo().getName().equals(findEmailRequestDTO.getName())) {
            throw new CustomException(Code.C504);
        } else {
            result.setMessage("FIND EMAIL SUCCESS");
            result.setResponseData("email", member.getEmail());
            result.setCode(200);
        }

        return result;

    }

    public ApiResponse memberGroup() {
        ApiResponse result = new ApiResponse();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!authentication.getPrincipal().equals("anonymousUser")) {
            Long memberId = Long.parseLong(((UserDetails) authentication.getPrincipal()).getUsername());

            Optional<Member> check = memberRepository.findById(memberId);

            if (check.isPresent()) {
                Member currUser = check.get();

                List<WrapperMy> wraperList = new LinkedList<>();

                List<Group> myGroupList = groupApplyLogRegistory.findAllByMemberId(memberId).stream().filter(x->x.getStatus()==1).map(x -> x.getGroup()).collect(Collectors.toList());

                for (Group g : myGroupList) {

                    MyGroup myGroup = MyGroup.builder()
                            .id(g.getId())
                            .name(g.getGroupName())
                            .description(g.getGroupInfo().getDescription())
                            .ctg(g.getCategory().stream().map(x -> x.getCategory().getName().toString()).collect(Collectors.toList()))
                            .imgLink(g.getGroupInfo().getImageLink())
                            .build();


                    // myData
                    // myData.studyTime
                    List<Long> roomIdList = g.getSprints().stream().map(x -> x.getRoom().getId()).collect(Collectors.toList());
                    List<MemberRoomLog> memberRoomLogList = mrlRepository.findByMemberIdAndRoomIdIn(currUser.getId(), roomIdList);

                    int studyTime = 0;
                    for (MemberRoomLog mrl :
                            memberRoomLogList) {
                        studyTime += mrl.getStudyTime();
                    }

                    // myData.penalty
                    List<Integer> penalty = new ArrayList<>();

                    List<PenaltyLog> penaltyLogList = penaltyLogRegistory.findAllByMemberIdAndRoomIn(currUser.getId(), g.getSprints().stream().map(x->x.getRoom()).collect(Collectors.toList()));

                    for (Mode m : Mode.values()) {
                        penalty.add(m.ordinal(), (int) (long) penaltyLogList.stream().filter(x -> x.getMode().ordinal() == m.ordinal()).count());
                    }

                    MemberGroup currMemberGroup = memberGroupRepository.findByMemberIdAndGroupId(currUser.getId(), g.getId()).get();

                    MyData myData = MyData.builder()
                            .studyTime(studyTime)
                            .penalty(penalty)
                            .joinDate(format.format(currMemberGroup.getCreatedAt()))
                            .build();

                    wraperList.add(WrapperMy.builder()
                            .myGroup(myGroup)
                            .myData(myData)
                            .build());
                }

                result.setCode(200);
                result.setMessage("SUCCESS");
                result.setResponseData("myGroups", wraperList);

            } else {
                throw new CustomException(Code.C501);
            }

        } else {
            throw new CustomException(Code.C501);
        }


        return result;
    }
        public ApiResponse findPW (FindPwDTO resetPwDTO){
            ApiResponse result = new ApiResponse();

            Member member = memberRepository.findByEmail(resetPwDTO.getEmail());

            if (member == null || !member.getMemberInfo().getName().equals(resetPwDTO.getName())) {
                throw new CustomException(Code.C504);
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
                    + "<input type='hidden' name='emailKey' value= '" + uuid + "'/> "
                    + "<input type='submit' value='비밀번호 초기화'/>"
                    + "</form></div>";
            mailService.sendMail(email, subject, text);
            result.setMessage("EMAIL SEND SUCCESS");
            return result;
        }

        public ApiResponse resetPW (ResetPwDTO resetPwDTO){
            ApiResponse result = new ApiResponse();
            MemberEmailKey memberEmailKey = memberEmailKeyRepository.findByEmailKey(resetPwDTO.getEmailKey());
            if(memberEmailKey == null){
                throw new CustomException(Code.C505);
            }

                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.DATE, -1);

                if (memberEmailKey.getCreatedAt().compareTo(cal.getTime()) < 0) {
                    // 유효 기간을 넘겼다.
                    // 삭제 : 이메일 초기화 키
                    // 시간 제한을 넘긴 인증 코드 삭제
                    memberEmailKeyRepository.deleteById(memberEmailKey.getId());

                    throw new CustomException(Code.C506);

                }
                Member member;
                try{
                    member = memberRepository.findById(memberEmailKey.getId()).get();
                } catch (Exception e){
                    throw new CustomException(Code.C504);
                }

                    String encPassword = bCryptPasswordEncoder.encode(resetPwDTO.getPassword());
                    member.setPwd(encPassword);

                    memberEmailKeyRepository.deleteById(member.getId());

                    result.setMessage("PASSWORD RESET SUCCESS");
                    result.setCode(200);
                    return result;
        }

        public ApiResponse resetPwMp (ResetPwMpDTO resetPwMpDTO){
            ApiResponse result = new ApiResponse();
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                    Long id;
                    try{
                        id = Long.parseLong(((UserDetails) authentication.getPrincipal()).getUsername());
                    } catch(Exception e){
                        throw new CustomException(Code.C501);
                    }
                    Member member = memberRepository.findById(id).get();


                    if (bCryptPasswordEncoder.matches(resetPwMpDTO.getPassword(), member.getPwd())) {

                        String encPassword = bCryptPasswordEncoder.encode(resetPwMpDTO.getNewPassword());

                        member.setPwd(encPassword);

                        result.setMessage("RESET PASSWORD SUCCESS");
                        result.setCode(200);
                    } else {
                        throw new CustomException(Code.C513);
                    }

            return result;
        }

        @Transactional
        public ApiResponse memberUpdate (UpdateRequestDTO updateRequestDTO, MultipartFile image){
            ApiResponse result = new ApiResponse();

            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (!authentication.getAuthorities().toString().equals("[ROLE_ANONYMOUS]")) {
                    if(nickNameCheckFunc(updateRequestDTO.getNickName())){
                        throw new CustomException(Code.C515);
                    }
                    Optional<Member> currUser = memberRepository.findById(Long.parseLong(((UserDetails) (authentication.getPrincipal())).getUsername()));

                    if (currUser.isPresent()) {
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        result.setCode(200);
                        result.setMessage("UPDATE SUCCESS");

                    }
                }
            } catch (Exception e) {
                throw new CustomException(Code.C501);
            }

            return result;
        }

        public ApiResponse emailCheck (CheckEmailDTO checkEmailDTO){
            ApiResponse apiResponse = new ApiResponse();

            if(emailCheckFunc(checkEmailDTO.getEmail())){
                throw new CustomException(Code.C514);
            }
            apiResponse.setCode(200);
            apiResponse.setMessage("AVAILABLE EMAIL");
            return apiResponse;

        }

        public ApiResponse nickNameCheck (CheckNickNameDTO checkNickNameDTO){
            ApiResponse apiResponse = new ApiResponse();

            if(nickNameCheckFunc(checkNickNameDTO.getNickName())){
                throw new CustomException(Code.C515);
            }

            apiResponse.setCode(200);
            apiResponse.setMessage("AVAILABLE NiCK NAME");
            return apiResponse;

        }

    public ApiResponse getMySprints(Long memberId) {
        ApiResponse apiResponse = new ApiResponse();
        List<MemberGroup> memberGroupList = memberGroupRepository.findAllByMemberId(memberId);
        if(memberGroupList.isEmpty()){
            throw new CustomException(Code.C520);
        }
        List<SprintGetResDTO> sprintList = new LinkedList<>();
        for(MemberGroup memberGroup : memberGroupList){
            List<Sprint> sprints = sprintRepository.findAllByGroupId(memberGroup.getGroup().getId());
            if(sprints.isEmpty()){
                throw new CustomException(Code.C520);
            }
            for(Sprint sprint : sprints){
                if(sprint.getStatus().equals(Status.DELETE)){
                    continue;
                }
                int myTime = 0;
                int sumTime = 0;
                int myPenalty = 0;
                int kingTime=0;
                int count=0;
                if(memberId != -1){
                    myPenalty = penaltyLogRegistory.countByMemberIdAndRoomId(memberId, sprint.getRoom().getId());
                    Optional<MemberRoomLog> myData = mrlRepository.findByMember_idAndRoom_id(memberId, sprint.getRoom().getId());
                    if(myData.isPresent()) {
                        myTime = myData.get().getStudyTime();
                    }
                }
                Optional<Integer> summ = mrlRepository.getSprintData(sprint.getRoom().getId());
                Optional<MemberRoomLog> memberRoomLog = mrlRepository.findTopByRoomIdOrderByStudyTimeDesc(sprint.getRoom().getId());


                if(summ.isPresent()) {
                    sumTime = summ.get();
                }

                String nickName=sprint.getGroup().getLeader().getNickName();


                if(memberRoomLog.isPresent()){
                    nickName = memberRoomLog.get().getMember().getNickName();
                    kingTime = memberRoomLog.get().getStudyTime();
                    count = penaltyLogRegistory.countByMemberIdAndRoomId(memberRoomLog.get().getMember().getId(), sprint.getRoom().getId());
                }
                int sumPenalty = penaltyLogRegistory.countByRoomId(sprint.getRoom().getId());




                SprintGetResDTO sprintGetResDTO = new SprintGetResDTO().builder()
                        .sprintId(sprint.getId())
                        .sprintImg(sprint.getSprintInfo().getImg())
                        .name(sprint.getName())
                        .description(sprint.getSprintInfo().getDescription())
                        .goal(sprint.getSprintInfo().getGoal())
                        .mode(sprint.getRoom().getMode())
                        .endAt(format.format(sprint.getSprintInfo().getEndAt()))
                        .penaltyMoney(sprint.getSprintInfo().getPenaltyMoney())
                        .startAt(format.format(sprint.getSprintInfo().getStartAt()))
                        .routineEndAt(format2.format(sprint.getSprintInfo().getRoutineEndAt()))
                        .routineStartAt(format2.format(sprint.getSprintInfo().getRoutineEndAt()))
                        .status(sprint.getStatus())
                        .kingName(nickName)
                        .kingPenalty(count)
                        .kingStudy(kingTime)
                        .studySum(sumTime)
                        .penaltySum(sumPenalty)
                        .myPenalty(myPenalty)
                        .myStudy(myTime)
                        .fee(sprint.getSprintInfo().getFee())
                        .build();
                sprintList.add(sprintGetResDTO);
            }
        }
        apiResponse.setCode(200);
        apiResponse.setMessage("SUCCESS MY SPRINT");
        apiResponse.setResponseData("sprints", sprintList);
        return  apiResponse;
    }

    // 멤버 정보를 가지고 오는 서비스 함수
    public ApiResponse getMyPage(Member currUser, HttpServletResponse response) {
        ApiResponse result = new ApiResponse();

        MemberInfo currUserInfo = currUser.getMemberInfo();

        // member
        MemberDTO resMemberDTO = MemberDTO.builder()
                .nickName(currUser.getNickName())
                .profileImage(currUserInfo.getImageLink())
                .point(currUserInfo.getPoint())
                .description(currUserInfo.getDescription())
                .studyTimeToday(currUserInfo.getStudyTimeDay())
                .studyTimeWeek(currUserInfo.getStudyTimeWeek())
                .studyTimeMonth(currUserInfo.getStudyTimeMonth())
                .studyTimeTotal(currUserInfo.getStudyTime())
                .build();

        result.setResponseData("member", resMemberDTO);

        // rules
        List<String> resRules = new LinkedList<>();

        for (Mode m : Mode.values()) {
            resRules.add(m.toString());
        }

        result.setResponseData("rules", resRules);


        // penaltyByDay
        // 1.일자별 패널티
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, 0);

        int[] penaltyByDay = new int[Calendar.getInstance().get(Calendar.DATE)];

        List<PenaltyLog> myPenaltyLog = penaltyLogRegistory.findByMember_idAndCreatedAtAfter(currUser.getId(), Date.from(cal.toInstant()));

        myPenaltyLog.stream().forEach(x -> penaltyByDay[x.getCreatedAt().getDate() - 1] += 1);

        result.setResponseData("penaltyByDay", penaltyByDay);


        // studyByDay
        // 2.일자별 공부 시간
        int[] studyByDay = new int[Calendar.getInstance().get(Calendar.DATE)];

        List<MemberRoomLog> myStudyLog = mrlRepository.findByMember_idAndStartAtAfter(currUser.getId(), Date.from(cal.toInstant()));

        if(myStudyLog.size()!=0){
            myStudyLog.stream().forEach(x -> studyByDay[x.getStartAt().getDate() - 1] += x.getStudyTime());
        }

        result.setResponseData("studyByDay", studyByDay);


        // 종류별 패털티 횟수
        Map<String, Integer> resMyPenalty = new LinkedHashMap<>();

        for (Mode m :
                Mode.values()) {
            resMyPenalty.put(m.toString(), penaltyLogRegistory.countByMember_idAndMode(currUser.getId(),m));
        }

        result.setResponseData("penalty", resMyPenalty);

        result.setCode(200);
        result.setMessage("SUCCESS GET MEMBER INFO");

        return result;
    }


    public ApiResponse getMyPoint(Long id) {
        ApiResponse result = new ApiResponse();

        Member currUser = memberRepository.findById(id).get();

        PageRequest myPoint = PageRequest.of(0, 100);

        List<PointLog> pointLogListAll = pointLogRepository.findAllByMemberId(currUser.getId()).stream().collect(Collectors.toList());

        int chargePoint = 0;
        for (PointLog pl : pointLogListAll.stream().filter(x -> x.getSprint() == null).collect(Collectors.toList())) {
            chargePoint += pl.getPointValue();
        }
        ;

        int getPoint = 0;
        for (PointLog pl : pointLogListAll.stream().filter(x -> x.getSprint() != null && x.getPointValue() > 0).collect(Collectors.toList())) {
            getPoint += pl.getPointValue();
        }
        ;

        int losePoint = 0;
        for (PointLog pl : pointLogListAll.stream().filter(x -> x.getSprint() != null && x.getPointValue() < 0).collect(Collectors.toList())) {
            losePoint += pl.getPointValue();
        }
        ;
        losePoint = -losePoint;

        int sumPoint = chargePoint + getPoint - losePoint;

        List<PointLogResDTO> pointList = new LinkedList<>();
        List<PointLog> pointLogList = pointLogRepository.findAllByMemberIdOrderByCreatedAt(currUser.getId(), myPoint).stream().collect(Collectors.toList());

        for (PointLog pl : pointLogList) {
            pointList.add(PointLogResDTO.builder()
                    .date(format3.format(pl.getCreatedAt()))
                    .content(pl.getSprint() != null ? pl.getSprint().getName() : "충전/환급")
                    .point(pl.getPointValue())
                    .build());
        }

        result.setResponseData("sumPoint", sumPoint);
        result.setResponseData("chargePoint", chargePoint);
        result.setResponseData("getPoint", getPoint);
        result.setResponseData("losePoint", losePoint);
        result.setResponseData("pointList", pointList);

        result.setCode(200);
        result.setMessage("point 내역 반환성공");


        return result;
    }

    private boolean nickNameCheckFunc(String nickName) {
        Member member = memberRepository.findByNickName(nickName);
        if(member != null) return true;
        return false;
    }

    private boolean emailCheckFunc(String email) {
        Member member = memberRepository.findByEmail(email);
        if(member != null) return true;
        return false;
    }
}

