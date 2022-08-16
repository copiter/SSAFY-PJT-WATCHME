package com.A108.Watchme.Service;

import com.A108.Watchme.DTO.Sprint.SprintGetResDTO;
import com.A108.Watchme.DTO.Sprint.SprintPostDTO;
import com.A108.Watchme.Exception.CustomException;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Http.Code;
import com.A108.Watchme.Repository.*;
import com.A108.Watchme.VO.ENUM.CategoryList;
import com.A108.Watchme.VO.ENUM.Mode;
import com.A108.Watchme.VO.ENUM.Status;
import com.A108.Watchme.VO.Entity.Category;
import com.A108.Watchme.VO.Entity.MemberGroup;
import com.A108.Watchme.VO.Entity.group.Group;
import com.A108.Watchme.VO.Entity.log.MemberRoomLog;
import com.A108.Watchme.VO.Entity.log.MemberSprintLog;
import com.A108.Watchme.VO.Entity.log.PointLog;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.VO.Entity.room.Room;
import com.A108.Watchme.VO.Entity.room.RoomInfo;
import com.A108.Watchme.VO.Entity.sprint.Sprint;
import com.A108.Watchme.VO.Entity.sprint.SprintInfo;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SprintService {
    private final RoomRepository roomRepository;
    private final RoomInfoRepository roomInfoRepository;
    private final SprintRepository sprintRepository;
    private final CategoryRepository categoryRepository;
    private final MGRepository mgRepository;
    private final MRLRepository mrlRepository;
    private final MemberRepository memberRepository;
    private final PenaltyLogRegistory penaltyLogRegistory;
    private final SprintInfoRepository sprintInfoRepository;
    private final GroupRepository groupRepository;
    private final PointLogRepository pointLogRepository;
    private final MSLRepository mslRepository;

    RoomService roomService;
    private final S3Uploader s3Uploader;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
    SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Transactional(rollbackFor = {Exception.class})
    public ApiResponse deleteSprint(Long sprintId, Long memberId) {
        Sprint sprint;

        ApiResponse apiResponse = new ApiResponse();


        try{
            sprint = sprintRepository.findById(sprintId).get();
        } catch (Exception e){
            throw new CustomException(Code.C533);
        }
        if(sprint.getStatus().equals(Status.DELETE)){
            throw new CustomException(Code.C571);
        }

        // 이미 시작한 거면 삭제 X
        if(!sprint.getStatus().equals(Status.YES)){
            throw new CustomException(Code.C543);
        }

        // 리더가 아닌 경우
        if (sprint.getGroup().getLeader().getId() != memberId) {
            throw new CustomException(Code.C536);
        }

        sprint.setStatus(Status.DELETE);

        List<MemberSprintLog> mslList = mslRepository.findAllBySprintId(sprintId);
        if(!mslList.isEmpty()){
            for(MemberSprintLog msl : mslList){
                msl.getMember().getMemberInfo().setPoint(
                        msl.getMember().getMemberInfo().getPoint() + sprint.getSprintInfo().getFee());

                pointLogRepository.save(PointLog.builder()
                        .sprint(sprint)
                        .member(msl.getMember())
                        .createdAt(new Date())
                        .pointValue(sprint.getSprintInfo().getFee())
                        .build());
            }
        }

        apiResponse.setCode(200);
        apiResponse.setMessage("DELETE SUCCESS");
        return apiResponse;
    }

    @Transactional(rollbackFor = {Exception.class})
    public ApiResponse createSprints(Long groupId, MultipartFile images, SprintPostDTO sprintPostDTO, Long memberId) {
        Group group;
        ApiResponse apiResponse = new ApiResponse();

        try{
            group = groupRepository.findById(groupId).get();
        } catch (Exception e){
            throw new CustomException(Code.C510);
        }

        Optional<Sprint> existSprint = sprintRepository.findByGroupIdAndStatus(groupId, Status.YES);

        if(existSprint.isPresent()){
            throw new CustomException(Code.C573);
        }

        Optional<Sprint> runningSprint = sprintRepository.findByGroupIdAndStatus(groupId, Status.ING);

        if(runningSprint.isPresent()){
            try{
                Date startDate = format.parse(sprintPostDTO.getStartAt());
                Date endDate = format.parse(sprintPostDTO.getEndAt());

                if(!runningSprint.get().getSprintInfo().getEndAt().before(startDate)){
                    throw new CustomException(Code.C544);
                }
                if(startDate.before(DateTime.now().toDate())){
                    throw new CustomException(Code.C545);
                }
                if(!startDate.before(endDate)){
                    throw new CustomException(Code.C546);
                }

            } catch(Exception e){
                throw new CustomException(Code.C599);
            }
        }

        Category category = categoryRepository.findByName(CategoryList.valueOf("스프린트"));
        Member member = memberRepository.findById(memberId).get();

        // 기본 이미지
        String url = "https://popoimages.s3.ap-northeast-2.amazonaws.com/Watchme/sprint.jpg";

        if (group.getLeader().getId() != memberId) {
            throw new CustomException(Code.C536);
        }
        if(images!=null){
            try {
                url = s3Uploader.upload(images, "Watchme");
            } catch (Exception e) {
                throw new CustomException(Code.C512);
            }
        }


            try {
                // 룸생성
                Room room = Room.builder()
                        .roomName(sprintPostDTO.getName())
                        .member(member)
                        .roomCtg(category)
                        .mode(Mode.valueOf(sprintPostDTO.getMode()))
                        .status(Status.SPR)
                        .view(0)
                        .build();

                RoomInfo roominfo = RoomInfo.builder()
                        .room(room)
                        .maxMember(group.getGroupInfo().getMaxMember())
                        .pwd(-1)
                        .currMember(0)
                        .createdAt(new Timestamp(System.currentTimeMillis()))
                        .endAt(format.parse(sprintPostDTO.getEndAt()))
                        .description(sprintPostDTO.getDescription())
                        .imageLink(url)
                        .build();

                roominfo = roomInfoRepository.save(roominfo);

                Sprint sprint = Sprint.builder()
                        .room(roominfo.getRoom())
                        .group(group)
                        .name(sprintPostDTO.getName())
                        .sumPoint(0)
                        .status(Status.YES)
                        .build();
                SprintInfo sprintInfo = SprintInfo.builder()
                        .sprint(sprint)
                        .fee(sprintPostDTO.getFee())
                        .endAt(format.parse(sprintPostDTO.getEndAt()))
                        .startAt(format.parse(sprintPostDTO.getStartAt()))
                        .routineEndAt(format2.parse(sprintPostDTO.getRoutineEndAt()))
                        .routineStartAt(format2.parse(sprintPostDTO.getRoutineStartAt()))
                        .img(url)
                        .penaltyMoney(sprintPostDTO.getPenaltyMoney())
                        .goal(sprintPostDTO.getGoal())
                        .description(sprintPostDTO.getDescription())
                        .build();

                sprintInfoRepository.save(sprintInfo);



            } catch (ParseException e) {
                throw new CustomException(Code.C599);
            } catch (Exception e){
                throw new CustomException(Code.C500);
            }

        apiResponse.setCode(200);
        apiResponse.setMessage("CREATE SUCCESS");
        return apiResponse;
    }

    public ApiResponse getSprints(Long groupId){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ApiResponse apiResponse = new ApiResponse();
        Long memberId = -1L;
        if(!authentication.getAuthorities().toString().equals("[ROLE_ANONYMOUS]")) {
            memberId = Long.parseLong(((UserDetails) authentication.getPrincipal()).getUsername());
        }

        List<SprintGetResDTO> sprintGetResDTOList = new LinkedList<>();
        List<Sprint> sprintList = sprintRepository.findAllByGroupId(groupId);
        if(sprintList.isEmpty()){
            throw new CustomException(Code.C520);
        }

            for(Sprint sprint: sprintList) {
                if(sprint.getStatus().equals(Status.DELETE)){
                    continue;
                }
                int myTime = 0;
                int sumTime = 0;
                int myPenalty = 0;
                int kingTime=0;
                int count=0;
                boolean isJoined = false;
                if(memberId != -1){
                    myPenalty = penaltyLogRegistory.countByMemberIdAndRoomId(memberId, sprint.getRoom().getId());
                    Optional<MemberRoomLog> myData = mrlRepository.findByMember_idAndRoom_id(memberId, sprint.getRoom().getId());
                    if(myData.isPresent()) {
                        myTime = myData.get().getStudyTime();
                    }
                    Optional <MemberSprintLog> memberSprintLog = mslRepository.findByMemberIdAndSprintId(memberId, sprint.getId());
                    if(memberSprintLog.isPresent()&& memberSprintLog.get().getStatus().equals(Status.YES)){
                        isJoined = true;
                    }

                }

                Optional<Integer> summ = mrlRepository.getSprintData(sprint.getRoom().getId());
                Optional<MemberRoomLog> memberRoomLog = mrlRepository.findTopByRoomIdOrderByStudyTimeDesc(sprint.getRoom().getId());


                if(summ.isPresent()) {
                    System.out.println(summ.get());
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
                        .isJoined(isJoined)
                        .fee(sprint.getSprintInfo().getFee())
                        .build();
                sprintGetResDTOList.add(sprintGetResDTO);

            }


            apiResponse.setCode(200);
            apiResponse.setMessage("SUCCESS");
            apiResponse.setResponseData("sprints", sprintGetResDTOList);

            return apiResponse;


    }
    @Transactional(rollbackFor = {Exception.class})
    public ApiResponse joinSprints(Long sprintId, Long memberId){
        Sprint sprint;
        MemberGroup memberGroup;
        try {
            sprint = sprintRepository.findById(sprintId).get();
        } catch (Exception e){
            throw new CustomException(Code.C533);
        }
        if(!sprint.getStatus().equals(Status.YES)){
            throw new CustomException(Code.C537);
        }

        ApiResponse apiResponse = new ApiResponse();

        try{
            memberGroup = mgRepository.findByMemberIdAndGroupId(memberId, sprint.getGroup().getId()).get();
        } catch(Exception e){
            throw new CustomException(Code.C535);
        }


        // 포인트 있을시에만
        Member member = memberRepository.findById(memberId).get();
        int point = member.getMemberInfo().getPoint();
        if(point < sprint.getSprintInfo().getFee()){
            throw new CustomException(Code.C538);
        }



        // 스프린트에 참가 로그 남김
        Optional<MemberSprintLog> memberSprintLog = mslRepository.findByMemberIdAndSprintId(memberId, sprintId);
        if(memberSprintLog.isPresent()&&memberSprintLog.get().getStatus().equals(Status.YES)){
            throw new CustomException(Code.C534);
        }
        if(!memberSprintLog.isPresent()){
            mslRepository.save(MemberSprintLog.builder()
                    .sprint(sprint)
                    .member(member)
                    .status(Status.YES)
                    .build());
        }
        if(!memberSprintLog.get().getStatus().equals(Status.YES)){
            memberSprintLog.get().setStatus(Status.YES);
        }

        member.getMemberInfo().setPoint(member.getMemberInfo().getPoint()-sprint.getSprintInfo().getFee());

        pointLogRepository.save(PointLog.builder()
                .sprint(sprint)
                .member(member)
                .createdAt(new Date())
                .pointValue(-1 * sprint.getSprintInfo().getFee())
                .build());

        apiResponse.setCode(200);
        apiResponse.setMessage("JOIN SUCCESS");
        return apiResponse;

    }
    @Transactional(rollbackFor = {Exception.class})
    public ApiResponse startSprints(Long sprintId, Long memberId) {
        Sprint sprint;
        try{
            sprint = sprintRepository.findById(sprintId).get();
        } catch (Exception e){
            throw new CustomException(Code.C533);
        }
        ApiResponse apiResponse = new ApiResponse();

        // 진행 중 일때만
        if(!sprint.getStatus().equals(Status.ING)){
            throw new CustomException(Code.C541);
        }
        // 신청했는지 확인
        Optional<MemberSprintLog> memberSprintLog = mslRepository.findByMemberIdAndSprintId(memberId, sprintId);
        if(!memberSprintLog.isPresent()){
            throw new CustomException(Code.C540);
        }

        if(memberSprintLog.get().getStatus().equals(Status.DELETE)){
            throw new CustomException(Code.C547);
        }
        roomService.roomPeople(memberSprintLog.get().getSprint().getRoom().getId(),1);
        roomService.joinRoomFunc(memberSprintLog.get().getSprint().getRoom().getId(),memberId);

        apiResponse.setCode(200);
        apiResponse.setMessage("SUCCESS START SPRINT");
        apiResponse.setResponseData("roomId",memberSprintLog.get().getSprint().getRoom().getId());

        return apiResponse;
    }


    public ApiResponse getPoints(Long sprintId, Long memberId) {
        ApiResponse apiResponse = new ApiResponse();
        Sprint sprint;
        Member member = memberRepository.findById(memberId).get();
        try{
            sprint = sprintRepository.findById(sprintId).get();
        } catch (Exception e){
            throw new CustomException(Code.C533);
        }
        if(!sprint.getStatus().equals(Status.NO)){
            throw new CustomException(Code.C570);
        }
        Optional<PointLog> myLog = pointLogRepository.findByMemberIdAndSprintIdAndFinish(memberId, sprintId, 1);
        // 정산 받았으면
        if(myLog.isPresent()){
            throw new CustomException(Code.C569);
        }
        List<MemberSprintLog> memberSprintLogs = mslRepository.findAllBySprintIdAndStatus(sprintId, Status.YES);
        List<PointLog> pointLogs = pointLogRepository.findAllByMemberIdAndSprintId(memberId, sprintId);
        if(pointLogs.isEmpty()){
            throw new CustomException(Code.C523);
        }

        int points = 0;
        for(PointLog pointLog : pointLogs){
            points += pointLog.getPointValue();
        }
        points += (2*sprint.getSprintInfo().getFee());
        points += (sprint.getSumPoint()/memberSprintLogs.size());
        pointLogRepository.save(PointLog.builder()
                .pointValue(points)
                        .createdAt(new Date())
                        .sprint(sprint)
                        .member(member)
                        .finish(1)
                .build());
        int myPoint = member.getMemberInfo().getPoint();
        member.getMemberInfo().setPoint(myPoint+points);
        apiResponse.setCode(200);
        apiResponse.setMessage("Success");
        apiResponse.setResponseData("points", points);
        return apiResponse;
    }

    public ApiResponse cancelSprint(Long sid, Long memberId) {
        ApiResponse apiResponse = new ApiResponse();
        MemberSprintLog sprintLog;
        Member member = memberRepository.findById(memberId).get();
        Sprint sprint = sprintRepository.findById(sid).get();
        try{
           sprintLog  = mslRepository.findByMemberIdAndSprintId(memberId, sid).get();
           if(!sprintLog.getStatus().equals(Status.YES)){
               throw new CustomException(Code.C523);
           }
        } catch (Exception e){
            throw new CustomException(Code.C523);
        }
        int fee = sprint.getSprintInfo().getFee();
        pointLogRepository.save(PointLog.builder()
                        .member(member)
                        .sprint(sprint)
                        .createdAt(new Date())
                        .pointValue(fee)
                .build());
        int myPoint = member.getMemberInfo().getPoint();
        member.getMemberInfo().setPoint(myPoint + fee);

        sprintLog.setStatus(Status.NO);
        apiResponse.setCode(200);
        apiResponse.setMessage("Success");

        return apiResponse;
    }
}
