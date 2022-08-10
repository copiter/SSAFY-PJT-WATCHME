package com.A108.Watchme.Service;

import com.A108.Watchme.DTO.Sprint.SprintData;
import com.A108.Watchme.DTO.Sprint.SprintGetResDTO;
import com.A108.Watchme.DTO.Sprint.SprintPostDTO;
import com.A108.Watchme.DTO.group.getGroup.SprintResDTO;
import com.A108.Watchme.Http.ApiResponse;
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
import org.checkerframework.checker.nullness.Opt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.TypedQuery;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SprintService {
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
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
    private final S3Uploader s3Uploader;

    private final PointLogRepository pointLogRepository;

    RoomService roomService;
    private final MSLRepository mslRepository;
    public ApiResponse deleteSprint(Long sprintId) {
        ApiResponse apiResponse = new ApiResponse();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(((UserDetails)authentication.getPrincipal()).getUsername());

        Optional<Sprint> sprint = sprintRepository.findById(sprintId);

        if(!sprint.isPresent()){
            apiResponse.setCode(400);
            apiResponse.setMessage("THERE IS NO SPRINT");
            return apiResponse;
        }

        // 이미 시작한 거면 삭제 X
        if(!sprint.get().getStatus().equals(Status.YES)){
            apiResponse.setCode(400);
            apiResponse.setMessage("ALREADY STARTED");
            return apiResponse;
        }

        // 리더가 아닌 경우
        if (sprint.get().getGroup().getLeader().getId() != memberId) {
            apiResponse.setCode(507);
            apiResponse.setMessage("INVALID AUTHENTICATION");
            return apiResponse;
        }

        sprint.get().setStatus(Status.DELETE);


        List<MemberSprintLog> mslList = mslRepository.findAllBySprintId(sprintId);

        for(MemberSprintLog msl : mslList){
            msl.getMember().getMemberInfo().setPoint(
                    msl.getMember().getMemberInfo().getPoint() + sprint.get().getSprintInfo().getFee());

            pointLogRepository.save(PointLog.builder()
                    .sprint(sprint.get())
                    .member(msl.getMember())
                    .createdAt(new Date())
                    .pointValue(sprint.get().getSprintInfo().getFee())
                    .build());
        }
        apiResponse.setCode(200);
        apiResponse.setMessage("DELETE SUCCESS");
        return apiResponse;
    }

    public ApiResponse createSprints(Long groupId, MultipartFile images, SprintPostDTO sprintPostDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ApiResponse apiResponse = new ApiResponse();
        Long memberId = Long.parseLong(((UserDetails) authentication.getPrincipal()).getUsername());
        Group group = groupRepository.findById(groupId).get();
        Category category = categoryRepository.findByName(CategoryList.valueOf("스프린트"));
        Member member = memberRepository.findById(memberId).get();
        String url = "https://popoimages.s3.ap-northeast-2.amazonaws.com/StudyRoom.jpg";

        if (group.getLeader().getId() != memberId) {
            apiResponse.setCode(507);
            apiResponse.setMessage("INVALID AUTHENTICATION");
            return apiResponse;
        }

        if (!authentication.getAuthorities().toString().equals("[ROLE_ANONYMOUS]")) {
            try {
                url = s3Uploader.upload(images, "Watchme");
            } catch (Exception e) {
                e.printStackTrace();
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
                        .endAt(format.parse(sprintPostDTO.getEndAt()))
                        .description(sprintPostDTO.getDescription())
                        .imageLink(url)
                        .display(0)
                        .build();

                roomRepository.save(room);
                roomInfoRepository.save(roominfo);

                Sprint sprint = Sprint.builder()
                        .room(room)
                        .group(group)
                        .name(sprintPostDTO.getName())
                        .sumPoint(0)
                        .build();
                SprintInfo sprintInfo = SprintInfo.builder()
                        .sprint(sprint)
                        .fee(sprintPostDTO.getFee())
                        .endAt(format.parse(sprintPostDTO.getEndAt()))
                        .startAt(format.parse(sprintPostDTO.getStartAt()))
                        .routineEndAt(format2.parse(sprintPostDTO.getRoutineEndAt()))
                        .routineStartAt(format2.parse(sprintPostDTO.getRoutineEndAt()))
                        .img(url)
                        .penaltyMoney(sprintPostDTO.getPenaltyMoney())
                        .goal(sprintPostDTO.getGoal())
                        .description(sprintPostDTO.getDescription())
                        .build();

                sprintRepository.save(sprint);
                sprintInfoRepository.save(sprintInfo);



            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        apiResponse.setCode(200);
        apiResponse.setMessage("CREATE SUCCESS");
        return apiResponse;
    }

    public ApiResponse getSprints(Long groupId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        ApiResponse apiResponse = new ApiResponse();
        Long memberId = Long.parseLong(((UserDetails) authentication.getPrincipal()).getUsername());
        List<SprintGetResDTO> sprintGetResDTOList = new LinkedList<>();
        List<Sprint> sprintList = sprintRepository.findAllByGroupId(groupId);

            for(Sprint sprint: sprintList) {
                Optional<MemberRoomLog> myData = mrlRepository.findByMemberIdAndRoomId(memberId, sprint.getRoom().getId());
                Optional<Integer> summ = mrlRepository.getSprintData(sprint.getRoom().getId());
                Optional<MemberRoomLog> memberRoomLog = mrlRepository.findTopByRoomIdOrderByStudyTimeDesc(sprint.getRoom().getId());

                int sumTime = 0;
                if(summ.isPresent()) {
                    System.out.println(summ.get());
                    sumTime = summ.get();
                }
                String nickName=sprint.getGroup().getLeader().getNickName();
                Integer kingTime=0;
                Integer count=0;

                if(memberRoomLog.isPresent()){
                    nickName = memberRoomLog.get().getMember().getNickName();
                    kingTime = memberRoomLog.get().getStudyTime();
                    count = penaltyLogRegistory.countByMemberIdAndRoomId(memberRoomLog.get().getMember().getId(), sprint.getRoom().getId());
                }
                int sumPenalty = penaltyLogRegistory.countByRoomId(sprint.getRoom().getId());
                int myTime = 0;
                int myPenalty = penaltyLogRegistory.countByMemberIdAndRoomId(memberId, sprint.getRoom().getId());
                if(myData.isPresent()){
                    myTime = myData.get().getStudyTime();
                }


                SprintGetResDTO sprintGetResDTO = new SprintGetResDTO().builder()
                        .sprintId(sprint.getId())
                        .sprintImg(sprint.getSprintInfo().getImg())
                        .sprintName(sprint.getName())
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
                sprintGetResDTOList.add(sprintGetResDTO);

            }


            apiResponse.setCode(200);
            apiResponse.setMessage("SUCCESS");
            apiResponse.setResponseData("sprints", sprintGetResDTOList);

            return apiResponse;


    }
    public ApiResponse joinSprints(Long sprintId){
        Sprint sprint = sprintRepository.findById(sprintId).get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(((UserDetails) authentication.getPrincipal()).getUsername());
        ApiResponse apiResponse = new ApiResponse();

        // 참가 가능일때만
        if(!sprint.getStatus().equals(Status.YES)){
            apiResponse.setCode(511);
            apiResponse.setMessage("JOIN IMPOSSIBLE");
            return apiResponse;
        }
        // 그룹원인지 확인
        Optional<MemberGroup> memberGroup = mgRepository.findByMemberIdAndGroupId(memberId, sprint.getGroup().getId());
        if(!memberGroup.isPresent()){
            apiResponse.setCode(512);
            apiResponse.setMessage("NOT GROUP MEMBER");
            return apiResponse;
        }

        // 포인트 있을시에만
        Member member = memberRepository.findById(memberId).get();
        int point = member.getMemberInfo().getPoint();
        if(point < sprint.getSprintInfo().getFee()){
            apiResponse.setCode(513);
            apiResponse.setMessage("NOT ENOUGH POINT");
            return apiResponse;
        }



        // 스프린트에 참가 로그 남김
        Optional<MemberSprintLog> memberSprintLog = mslRepository.findByMemberIdAndSprintId(memberId, sprintId);
        if(memberSprintLog.isPresent()){
            apiResponse.setCode(514);
            apiResponse.setMessage("ALREADY JOIN");
            return apiResponse;
        }
        mslRepository.save(MemberSprintLog.builder()
                .sprint(sprint)
                .member(member)
                .build());
        // 포인트 차감
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

    public ApiResponse startSprints(Long sprintId) {
        Sprint sprint = sprintRepository.findById(sprintId).get();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(((UserDetails) authentication.getPrincipal()).getUsername());
        ApiResponse apiResponse = new ApiResponse();

        // 진행 중 일때만
        if(!sprint.getStatus().equals(Status.ING)){
            apiResponse.setCode(511);
            apiResponse.setMessage("JOIN IMPOSSIBLE");
            return apiResponse;
        }
        // 신청했는지 확인
        Optional<MemberSprintLog> memberSprintLog = mslRepository.findByMemberIdAndSprintId(memberId, sprintId);
        if(!memberSprintLog.isPresent()){
            apiResponse.setCode(512);
            apiResponse.setMessage("NOT APPLY");
            return apiResponse;
        }

        roomService.joinRoomFunc(memberSprintLog.get().getSprint().getRoom().getId());
        apiResponse.setCode(200);
        apiResponse.setMessage("SUCCESS");

        return apiResponse;
    }



}
