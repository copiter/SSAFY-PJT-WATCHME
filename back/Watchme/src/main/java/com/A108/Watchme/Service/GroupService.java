package com.A108.Watchme.Service;

import com.A108.Watchme.utils.AuthUtil;
import com.A108.Watchme.DTO.*;
import com.A108.Watchme.DTO.group.*;
import com.A108.Watchme.DTO.group.getGroup.*;
import com.A108.Watchme.DTO.group.getGroupList.GroupListResDTO;
import com.A108.Watchme.DTO.group.getGroupList.SprintDTO;
import com.A108.Watchme.Exception.CustomException;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Http.Code;
import com.A108.Watchme.Repository.*;
import com.A108.Watchme.VO.ENUM.*;
import com.A108.Watchme.VO.Entity.Category;
import com.A108.Watchme.VO.Entity.MemberGroup;
import com.A108.Watchme.VO.Entity.group.Group;
import com.A108.Watchme.VO.Entity.group.GroupCategory;
import com.A108.Watchme.VO.Entity.group.GroupInfo;
import com.A108.Watchme.VO.Entity.log.GroupApplyLog;
import com.A108.Watchme.VO.Entity.log.MemberRoomLog;
import com.A108.Watchme.VO.Entity.log.PenaltyLog;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.VO.Entity.sprint.Sprint;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepos;
    private final CategoryRepository categoryRepos;
    private final GroupCategoryRepository groupCategoryRepos;
    private final GroupInfoRepository groupInfoRepos;
    private final GroupApplyLogRegistory galRepos;
    private final PenaltyLogRegistory plRepos;
    private final MemberRepository memberRepos;
    private final MemberGroupRepository memberGroupRepos;
    private final MRLRepository mrlRepos;
    private final SprintRepository sprintRepos;

    private final S3Uploader s3Uploader;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private AuthUtil authUtil;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");

    public ApiResponse getGroupList(String ctgName, String keyword, Integer page, HttpServletRequest request) {
        ApiResponse result = new ApiResponse();

        // Status가 YES인 그룹 조회
        List<Group> groupList;

        if (page == null) {
            page = 1;
        }
        // TODO : 10개씩 보여주는 거 맞는지?
        PageRequest pageRequest = PageRequest.of(page - 1, 10);

        if (ctgName != null) {
            Category category = categoryRepos.findByName(CategoryList.valueOf(ctgName));

            if (keyword == null) {
                groupList = groupRepos.findAllByCategory_category(category, pageRequest).stream().collect(Collectors.toList());

            } else {
                groupList = groupRepos.findAllByCategory_categoryAndGroupNameContaining(category, keyword, pageRequest).stream().collect(Collectors.toList());
            }

        } else {
            if (keyword == null) {
                groupList = groupRepos.findAllByOrderByViewDesc(pageRequest).stream().collect(Collectors.toList());

            } else {
                groupList = groupRepos.findAllByGroupNameContaining(keyword, pageRequest).stream().collect(Collectors.toList());
            }

        }

        // 삭제된 그룹 필터링
        groupList = groupList.stream().filter(x -> x.getStatus() == Status.YES).collect(Collectors.toList());

        // 조회된 내역이 없는 경우
        if (groupList.isEmpty()) {
            throw new CustomException(Code.C520);
        }

        // 조회된 내역이 있는 경우
        // getGroupList(Res DTO) : 그룹 리스트 반환
        List<GroupListResDTO> getGroupList = new LinkedList<>();

        for (Group g : groupList) {
            // sprint : 진행할 예정인 sprint
            List<Sprint> sprint = g.getSprints().stream().filter(x -> x.getSprintInfo().getStartAt().after(new Date())).collect(Collectors.toList());

            // sprint 중 첫번째 항목을 반환 : 프론트 요구에 따라 배열로 전달할 수도 있겠다.
            Sprint currSprint;

            // 반환 DTO List에 추가
            getGroupList.add(GroupListResDTO.builder()
                    .id(g.getId())
                    .name(g.getGroupName())
                    .description(g.getGroupInfo().getDescription())
                    .currMember(g.getGroupInfo().getCurrMember())
                    .maxMember(g.getGroupInfo().getMaxMember())
                    .ctg(g.getCategory().stream().map(x -> x.getCategory().getName().toString()).collect(Collectors.toList()))
                    .imgLink(g.getGroupInfo().getImageLink())
                    .createdAt(format.format(g.getCreatedAt()))
                    .secret(g.getSecret() == 1 ? true : false)
                    .view(g.getView())
                    // 현재 진행중인 sprint가 있다면
                    .sprint(!sprint.isEmpty() ?
                            SprintDTO.builder()
                                    .name((currSprint = sprint.get(0)).getName())
                                    .description(currSprint.getSprintInfo().getDescription())
                                    .startAt(format.format(currSprint.getSprintInfo().getStartAt()))
                                    .endAt(format.format(currSprint.getSprintInfo().getEndAt()))
                                    .status(currSprint.getStatus().toString())
                                    .build() : null
                    )
                    .build()
            );
        }

        result.setResponseData("groups", getGroupList);


        result.setCode(200);
        result.setMessage("GETROOMS SUCCESS");

        return result;
    }

    public ApiResponse getGroup(Long groupId, String pwd) {
        ApiResponse result = new ApiResponse();

        // 그룹 존재여부 체크
        Group group = checkGroup(groupId);

        // 삭제된 group의 경우
        if (group.getStatus() == Status.NO) {
            throw new CustomException(Code.C510);
        }

        // 반환 DTO 생성 및 추가 : GroupResDTO : 그룹 정보
        result.setResponseData("group", GroupResDTO.builder()
                .name(group.getGroupName())
                .description(group.getGroupInfo().getDescription())
                .currMember(group.getGroupInfo().getCurrMember())
                .maxMember(group.getGroupInfo().getMaxMember())
                .ctg(group.getCategory().stream().map(x -> x.getCategory().getName().toString()).collect(Collectors.toList()))
                .imgLink(group.getGroupInfo().getImageLink())
                .createAt(format.format(group.getCreatedAt()))
                .display(group.getSecret())
                .view(group.getView())
                .build());


        // sprintResDTOList : 반환 DTO List
        List<SprintResDTO> sprintResDTOList;

        List<Sprint> sprintList = sprintRepos.findAllByGroupId(groupId);

        if (!sprintList.isEmpty()) {
            sprintResDTOList = new LinkedList<>();

            for (Sprint sprint : sprintList) {
                // 스프린트에 참가중인 그룹원들의 총 공부시간
                int sumTime = 0;
                Optional<Integer> sum = mrlRepos.getSprintData(sprint.getRoom().getId());
                if (sum.isPresent()) {
                    sumTime = sum.get();
                }

                // 스터디 킹의 닉네임 및 방에서 공부한 시간 및 받은 페널티
                String nickName = sprint.getGroup().getLeader().getNickName();
                Integer kingTime = 0;
                Integer count = 0;

                Optional<MemberRoomLog> checkMrl = mrlRepos.findTopByRoomIdOrderByStudyTimeDesc(sprint.getRoom().getId());
                if (checkMrl.isPresent()) {
                    MemberRoomLog memberRoomLog = checkMrl.get();

                    nickName = memberRoomLog.getMember().getNickName();
                    kingTime = memberRoomLog.getStudyTime();
                    count = plRepos.countByMemberIdAndRoomId(memberRoomLog.getMember().getId(), sprint.getRoom().getId());
                }

                // 방에서 생성된 총 패널티 수
                int sumPenalty = plRepos.countByRoomId(sprint.getRoom().getId());

                sprintResDTOList.add(new SprintResDTO().builder()
                        .sprintId(sprint.getId())
                        .sprintImg(sprint.getSprintInfo().getImg())
                        .name(sprint.getName())
                        .description(sprint.getSprintInfo().getDescription())
                        .goal(sprint.getSprintInfo().getGoal())
                        .mode(sprint.getRoom().getMode().toString())
                        .endAt(format.format(sprint.getSprintInfo().getEndAt()))
                        .penaltyMoney(sprint.getSprintInfo().getPenaltyMoney())
                        .startAt(format.format(sprint.getSprintInfo().getStartAt()))
                        .routineEndAt(format2.format(sprint.getSprintInfo().getRoutineEndAt()))
                        .routineStartAt(format2.format(sprint.getSprintInfo().getRoutineEndAt()))
                        .status(sprint.getStatus().toString())
                        .kingName(nickName)
                        .kingPenalty(count)
                        .kingStudy(kingTime)
                        .studySum(sumTime)
                        .penaltySum(sumPenalty)
                        .build());
            }

            result.setResponseData("sprints", sprintResDTOList);
        }


        // leader
        Member leader = group.getLeader();

        result.setResponseData("leader", new GroupMemberResDTO().builder()
                .nickName(leader.getNickName())
                .imgLink(leader.getMemberInfo().getImageLink())
                .role(GroupRole.LEADER.ordinal())
                .build());


        // 접속자 체크
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 비로그인
        if (((UserDetails) authentication.getPrincipal()).getUsername().equals("anonymousUser")) {
            // 비공개 그룹의 경우
            if (group.getSecret() == 1) {
                throw new CustomException(Code.C501);
            }

            // myData(비로그인)
            List<Integer> penalties = new ArrayList<>(Mode.values().length);
            Collections.fill(penalties, 0);

            result.setResponseData("myData", MyDataResDTO.builder()
                    .role(GroupRole.ANONYMOUS.ordinal())
                    .penalty(penalties)
                    .assign(0)
                    .studyTime(0)
                    .build());

            // groupData(비로그인)
            List<Long> roomIdList = group.getSprints().stream().map(x -> x.getRoom().getId()).collect(Collectors.toList());
            List<MemberRoomLog> groupRoomLogList = mrlRepos.findByRoomIdIn(roomIdList);
            // groupData.sumTime
            int sumTime = 0;
            for (MemberRoomLog mrl : groupRoomLogList) {
                sumTime += mrl.getStudyTime();
            }

            GroupDataResDTO groupDataResDTO = GroupDataResDTO.builder()
                    .sumTime(sumTime)
                    .build();

            result.setResponseData("groupData", groupDataResDTO);


            result.setCode(200);
            result.setMessage("GET GROUP SUCCESS");

        } else {
            // 로그인
            String currUserId = ((UserDetails) authentication.getPrincipal()).getUsername();

            Optional<Member> checkMember = memberRepos.findById(Long.parseLong(currUserId));
            List<Member> groupMembers = group.getMemberGroupList().stream().map(x -> x.getMember()).collect(Collectors.toList());

            // 그룹원 여부 체크
            if (checkMember.isPresent() && groupMembers.stream().anyMatch(x -> x.getEmail().equals(checkMember.get().getEmail()))) {
                // 그룹원 + 그룹장
                Member currMember = checkMember.get();

                // members
                List<GroupMemberResDTO> members = new LinkedList<>();

                for (Member m : groupMembers) {
                    members.add(GroupMemberResDTO.builder()
                            .nickName(m.getNickName())
                            .imgLink(m.getMemberInfo().getImageLink())
                            .role(m.getEmail().equals(leader.getEmail()) ? GroupRole.LEADER.ordinal() : GroupRole.MEMBER.ordinal())
                            .build()
                    );
                }

                result.setResponseData("members", members);


                // myData
                // myData.studyTime
                List<Long> roomIdList = group.getSprints().stream().map(x -> x.getRoom().getId()).collect(Collectors.toList());

                List<MemberRoomLog> memberRoomLogList = mrlRepos.findByMemberIdAndRoomIdIn(currMember.getId(), roomIdList);
                int studyTime = 0;

                for (MemberRoomLog mrl :
                        memberRoomLogList) {
                    studyTime += mrl.getStudyTime();
                }

                // myData.penalty
                List<Integer> penalty = new ArrayList<>(Mode.values().length);

                List<PenaltyLog> penaltyLogList = plRepos.findAllByMemberIdAndRoomIn(currMember.getId(), group.getSprints().stream().map(x -> x.getRoom()).collect(Collectors.toList()));

                for (Mode mode : Mode.values()) {
                    penalty.add(mode.ordinal(), (int) (long) penaltyLogList.stream().filter(x -> x.getMode().ordinal() == mode.ordinal()).count());
                }

                // myData.joinDate
                MemberGroup currMemberGroup = memberGroupRepos.findByMemberIdAndGroupId(currMember.getId(), groupId).get();

                result.setResponseData("myData", MyDataResDTO.builder()
                        .role(currMember.getEmail().equals(leader.getEmail()) ? GroupRole.LEADER.ordinal() : GroupRole.MEMBER.ordinal())
                        .studyTime(studyTime)
                        .penalty(penalty)
                        .joinDate(format.format(currMemberGroup.getCreatedAt()))
                        .build()
                );

                //groupdata
                List<MemberRoomLog> groupRoomLogList = mrlRepos.findByRoomIdIn(roomIdList);
                GroupDataResDTO groupDataResDTO;

                int sumTime = 0;
                for (MemberRoomLog mrl :
                        groupRoomLogList) {
                    sumTime += mrl.getStudyTime();
                }

                if (group.getLeader().getId().equals(currMember.getId())) {
                    int assignee = (int) galRepos.countByGroupIdAndStatus(groupId, 0);
                    groupDataResDTO = GroupDataResDTO.builder()
                            .sumTime(sumTime)
                            .assignee(assignee)
                            .build();
                } else {
                    groupDataResDTO = GroupDataResDTO.builder()
                            .sumTime(sumTime)
                            .build();
                }

                result.setResponseData("groupData", groupDataResDTO);

                result.setCode(200);
                result.setMessage("GET GROUP SUCCESS");

            } else if (checkMember.isPresent()) {
                if (group.getSecret() == 1) {
                    throw new CustomException(Code.C501);
                }

                // myData(비그릅원)
                List<Integer> penalties = new ArrayList<>(Mode.values().length);
                Collections.fill(penalties, 0);

                Optional<GroupApplyLog> gal = galRepos.findByMemberIdAndGroupId(checkMember.get().getId(), groupId);

                result.setResponseData("myData", MyDataResDTO.builder()
                        .role(GroupRole.ANONYMOUS.ordinal())
                        .assign(gal.isPresent() ? gal.get().getStatus() == 2 ? 2 : 1 : 0)
                        .penalty(penalties)
                        .studyTime(0)
                        .build());


                // groupData(비그릅원)
                List<Long> roomIdList = group.getSprints().stream().map(x -> x.getRoom().getId()).collect(Collectors.toList());
                List<MemberRoomLog> groupRoomLogList = mrlRepos.findByRoomIdIn(roomIdList);
                // groupData.sumTime
                int sumTime = 0;
                for (MemberRoomLog mrl : groupRoomLogList) {
                    sumTime += mrl.getStudyTime();
                }

                GroupDataResDTO groupDataResDTO = GroupDataResDTO.builder()
                        .sumTime(sumTime)
                        .build();

                result.setResponseData("groupData", groupDataResDTO);

                result.setCode(200);
                result.setMessage("GET GROUP SUCCESS");

            }
        }
        // members
        List<GroupMemberResDTO> members = new LinkedList<>();

        List<Member> groupMembers = group.getMemberGroupList().stream().map(x -> x.getMember()).collect(Collectors.toList());

        for (Member m : groupMembers) {
            members.add(GroupMemberResDTO.builder()
                    .nickName(m.getNickName())
                    .imgLink(m.getMemberInfo().getImageLink())
                    .role(m.getEmail().equals(leader.getEmail()) ? GroupRole.LEADER.ordinal() : GroupRole.MEMBER.ordinal())
                    .build()
            );
        }

        result.setResponseData("members", members);

        return result;
    }

    @Transactional(rollbackFor = {Exception.class})
    public ApiResponse createGroup(GroupCreateReqDTO groupCreateReqDTO, MultipartFile image, HttpServletRequest request) {
        ApiResponse result = new ApiResponse();

        Long currUserId = authUtil.memberAuth();
        Member currUser = memberRepos.findById(currUserId).get();

        String url = "https://popoimages.s3.ap-northeast-2.amazonaws.com/WatchMe/groups.jpg";

        if (image != null) {
            try {
                url = s3Uploader.upload(image, "Watchme");
            } catch (Exception e) {
                throw new CustomException(Code.C512);
            }
        }

        try {
            // 1.group 기본 저장
            Group newGroup = Group.builder()
                    .groupName(groupCreateReqDTO.getName())
                    .leader(currUser)
                    .createdAt(new Date())
                    .status(Status.YES)
                    .view(0)
                    // TODO : secret을 받는다면 여기에서 set 해줄 것
                    .secret(0)
                    .build();


            GroupInfo newGroupInfo = GroupInfo.builder()
                    .group(newGroup)
                    .imageLink(url)
                    .description(groupCreateReqDTO.getDescription())
                    .currMember(1)
                    .maxMember(groupCreateReqDTO.getMaxMember())
                    .build();

            GroupInfo nGI = groupInfoRepos.save(newGroupInfo);


            // 2.MemberGroup
            MemberGroup newMemberGroup = MemberGroup.builder()
                    .group(nGI.getGroup())
                    .member(nGI.getGroup().getLeader())
                    .createdAt(new Date())
                    .groupRole(GroupRole.LEADER)
                    .build();


            // 3.GroupCategory
            List<GroupCategory> newGroupCategory = new LinkedList<>();

            for (String ctg :
                    groupCreateReqDTO.getCtg()) {
                newGroupCategory.add(GroupCategory.builder()
                        .category(categoryRepos.findByName(CategoryList.valueOf(ctg)))
                        .group(nGI.getGroup())
                        .build());
            }


            // 4.GroupApplyLog
            GroupApplyLog newGroupApplyLog = GroupApplyLog.builder()
                    .member(currUser)
                    .group(nGI.getGroup())
                    .apply_date(new Date())
                    .status(1)
                    .build();


            memberGroupRepos.save(newMemberGroup);
            groupCategoryRepos.saveAll(newGroupCategory);
            galRepos.save(newGroupApplyLog);

            result.setCode(200);
            result.setMessage("SUCCESS ADD&JOIN ROOM");
            result.setResponseData("groupId", nGI.getGroup().getId());
        } catch (Exception e) {
            throw new CustomException(Code.C500);
        }

        return result;
    }

    @Transactional(rollbackFor = {Exception.class})
    public ApiResponse updateGroup(Long groupId, GroupUpdateReqDTO groupUpdateReqDTO, MultipartFile image, HttpServletRequest request) {
        ApiResponse result = new ApiResponse();

        Long currUserId = authUtil.memberAuth();
        Member currUser = memberRepos.findById(currUserId).get();

        Group group = checkGroup(groupId);
        GroupInfo groupInfo = groupInfoRepos.findById(groupId).get();

        if (currUser.getId() != group.getLeader().getId()) {
            throw new CustomException(Code.C565);
        }

        String url = group.getGroupInfo().getImageLink();

        if (image != null) {
            try {
                url = s3Uploader.upload(image, "Watchme");
            } catch (Exception e) {
                throw new CustomException(Code.C512);
            }
        }

        // 그룹 카테고리 삭제
        List<GroupCategory> groupCategoryList = groupCategoryRepos.findAllByGroupId(groupId);
        groupCategoryRepos.deleteAllInBatch(groupCategoryList);

        try {
            // 그룹 카테코리 리스트 생성
            List<GroupCategory> categoryList = new LinkedList<>();

            try {
                for (String ctg : groupUpdateReqDTO.getCtg()) {
                    categoryList.add(GroupCategory.builder()
                            .category(categoryRepos.findByName(CategoryList.valueOf(ctg)))
                            .group(group)
                            .build());
                }
            } catch (Exception e) {
                throw new CustomException(Code.C521);
            }

            // update
            group.setGroupName(groupUpdateReqDTO.getName());

            // TODO : display? secret?
            group.setSecret(groupUpdateReqDTO.getDisplay());

            groupInfo.setDescription(groupUpdateReqDTO.getDescription());
            groupInfo.setMaxMember(Integer.parseInt(groupUpdateReqDTO.getMaxMember()));

            groupRepos.save(group);
            groupInfoRepos.save(groupInfo);
            groupCategoryRepos.saveAll(categoryList);
        } catch (Exception e) {
            throw new CustomException(Code.C500);
        }

        result.setCode(200);
        result.setMessage("SUCCESS GROUP UPDATE");

        return result;
    }

    public ApiResponse deleteGroup(Long groupId) {
        ApiResponse result = new ApiResponse();

        Long currUserId = authUtil.memberAuth();
        Member currUser = memberRepos.findById(currUserId).get();

        Group group = checkGroup(groupId);

        if (group.getLeader().getId() == currUserId) {
            // TODO : Status만 DELETE로 바꾸면 될까? 다른 곳의 로직 체크!! ex.삭제되었지만 수정은 가능?
            group.setStatus(Status.DELETE);

            result.setCode(200);
            result.setMessage("GROUP DELETE SUCCESS");
        } else {
            throw new CustomException(Code.C565);
        }

        return result;
    }

    public ApiResponse getApplyList(Long groupId) {
        ApiResponse result = new ApiResponse();

        Long currUserId = authUtil.memberAuth();
        Member currUser = memberRepos.findById(currUserId).get();

        Group group = checkGroup(groupId);


        // members
        List<GroupMemberResDTO> members = new LinkedList<>();

        List<Member> groupMembers = group.getMemberGroupList().stream().map(x -> x.getMember()).collect(Collectors.toList());

        for (Member m : groupMembers) {
            members.add(GroupMemberResDTO.builder()
                    .nickName(m.getNickName())
                    .build()
            );
        }

        result.setResponseData("members", members);


        // appliers
        if (currUser.getId() == group.getLeader().getId()) {
            // 그룹 리더임
            List<GroupApplyLog> applyLogs = galRepos.findAllByGroupId(groupId);

            applyLogs = applyLogs.stream().filter(x -> x.getStatus() != 0).collect(Collectors.toList());

            List<GroupApplyDTO> getApplies = new LinkedList<>();

            for (GroupApplyLog applyLog : applyLogs) {
                Member member = applyLog.getMember();

                //
                List<Integer> penalty = new ArrayList<>(Mode.values().length);
                List<PenaltyLog> penaltyLogList = plRepos.findAllByMemberId(member.getId());

                if (penaltyLogList.size() != 0) {
                    for (Mode mode :
                            Mode.values()) {
                        penalty.add(mode.ordinal(), (int) penaltyLogList.stream().filter(x -> x.getMode().ordinal() == mode.ordinal()).count());
                    }
                }

                //
                getApplies.add(new GroupApplyDTO().builder()
                        .email(member.getEmail()).nickName(member.getNickName())
                        .imgLink(member.getMemberInfo().getImageLink())
                        .studyTime(member.getMemberInfo().getStudyTime())
                        .penalty(penalty)
                        .build()
                );
            }

            result.setResponseData("appliers", getApplies);
        } else {
            result.setResponseData("appliers", null);
        }

        result.setCode(200);
        result.setMessage("GROUP APPLY LIST SUCCESS");

        return result;
    }

    @Transactional(rollbackFor = {Exception.class})
    public ApiResponse apply(Long groupId) {
        ApiResponse result = new ApiResponse();

        Long currUserId = authUtil.memberAuth();
        Member currUser = memberRepos.findById(currUserId).get();

        Group group = checkGroup(groupId);


        Optional<GroupApplyLog> groupApplyLog = galRepos.findByMemberIdAndGroupId(currUserId, groupId);
        List<Long> groupMembers = group.getMemberGroupList().stream().map(x -> x.getMember().getId()).collect(Collectors.toList());

        // 이미 멤버인 경우
        if (groupMembers.contains(currUserId)) {
            throw new CustomException(Code.C509);
        }

        if (groupApplyLog.isEmpty()) {
            galRepos.save(GroupApplyLog.builder()
                    .member(currUser)
                    .group(group)
                    .apply_date(new Date())
                    .status(0)
                    .build()
            );

            result.setCode(200);
            result.setMessage("GROUP JOIN APPLY SUCCESS");
        } else {
            if (groupApplyLog.get().getStatus() == 2) {
                throw new CustomException(Code.C566);
            } else if (groupApplyLog.get().getStatus() == 0) {
                throw new CustomException(Code.C567);
            } else {
                throw new CustomException(Code.C500);
            }

        }

        return result;
    }

    public ApiResponse cancelApply(Long groupId) {
        ApiResponse result = new ApiResponse();

        Optional<Group> group = groupRepos.findById(groupId);

        if (group.isPresent()) {
            Long currUserId = authUtil.memberAuth();

            Optional<Member> member = memberRepos.findById(currUserId);

            if (member.isPresent()) {
                Optional<GroupApplyLog> checkGroupApplyLog = galRepos.findByMemberIdAndGroupId(currUserId, groupId);

                if (checkGroupApplyLog.isPresent()) {
                    // 로그에서 삭제
                    GroupApplyLog groupApplyLog = checkGroupApplyLog.get();

                    if (groupApplyLog.getStatus() == 0) {
                        galRepos.delete(groupApplyLog);

                        result.setCode(200);
                        result.setMessage("GROUP APPLY CANCLE SUCCESS");
                    } else if (groupApplyLog.getStatus() == 1) {
                        throw new CustomException(Code.C509);
                    } else if (groupApplyLog.getStatus() == 2) {
                        throw new CustomException(Code.C566);
                    }
                } else {
                    throw new CustomException(Code.C300);
                }
            } else {
                throw new CustomException(Code.C503);
            }

        } else {
            throw new CustomException(Code.C510);
        }
        return result;
    }

    @Transactional
    public ApiResponse acceptApply(Long groupId, AcceptApplyReqDTO acceptApplyReqDTO) {
        ApiResponse result = new ApiResponse();

        Optional<Group> checkGroup = groupRepos.findById(groupId);

        if (checkGroup.isPresent()) {
            Group group = checkGroup.get();

            Long currUserId = authUtil.memberAuth();

            Optional<Member> checkCurrUser = memberRepos.findById(currUserId);

            if (checkCurrUser.isPresent()) {
                Member currUser = checkCurrUser.get();

                if (currUser.getId() == group.getLeader().getId()) {
                    Member applier = memberRepos.findByNickName(acceptApplyReqDTO.getNickName());

                    Optional<GroupApplyLog> groupApplyLog = galRepos.findByMemberIdAndGroupId(applier.getId(), groupId);

                    if (groupApplyLog.isPresent()) {
                        groupApplyLog.get().setStatus(1);
                        groupApplyLog.get().setUpdate_date(new Date());

                        galRepos.save(groupApplyLog.get());

                        memberGroupRepos.save(MemberGroup.builder()
                                .member(applier)
                                .group(group)
                                .createdAt(new Date())
                                .groupRole(GroupRole.MEMBER)
                                .build()
                        );

                        result.setCode(200);
                        result.setMessage("GROUP APPLY ACCEPT SUCCESS");
                    } else {
                        throw new CustomException(Code.C300);
                    }

                } else {
                    throw new CustomException(Code.C565);
                }
            } else {
                throw new CustomException(Code.C503);
            }
        } else {
            throw new CustomException(Code.C510);
        }

        return result;
    }

    public ApiResponse declineApply(Long groupId, DeclineApplyReqDTO declineApplyReqDTO) {
        ApiResponse result = new ApiResponse();

        Optional<Group> checkGroup = groupRepos.findById(groupId);

        if (checkGroup.isPresent()) {
            Group group = checkGroup.get();

            Long currUserId = authUtil.memberAuth();

            Optional<Member> checkCurrUser = memberRepos.findById(currUserId);

            if (checkCurrUser.isPresent()) {
                Member currUser = checkCurrUser.get();

                if (currUser.getId() == group.getLeader().getId()) {
                    Member member = memberRepos.findByNickName(declineApplyReqDTO.getNickName());

                    Optional<GroupApplyLog> groupApplyLog = galRepos.findByMemberIdAndGroupId(member.getId(), groupId);

                    if (groupApplyLog.isPresent()) {

                        if (groupApplyLog.get().getStatus() == 1) {
                            throw new CustomException(Code.C509);
                        } else {
                            galRepos.delete(groupApplyLog.get());
                            result.setCode(200);
                            result.setMessage("GROUP APPLY DECLINE SUCCESS");
                        }
                    } else {
                        throw new CustomException(Code.C300);
                    }

                } else {
                    throw new CustomException(Code.C565);
                }
            } else {
                throw new CustomException(Code.C503);
            }

        } else {
            throw new CustomException(Code.C510);
        }

        return result;
    }

    @Transactional
    public ApiResponse leaveGroup(Long groupId) {
        ApiResponse result = new ApiResponse();

        Optional<Group> checkGroup = groupRepos.findById(groupId);

        if (checkGroup.isPresent()) {
            Long currUserId = authUtil.memberAuth();

            Optional<Member> checkCurrUser = memberRepos.findById(currUserId);

            if (checkCurrUser.isPresent()) {
                Member currUser = checkCurrUser.get();

                Optional<GroupApplyLog> groupApplyLog = galRepos.findByMemberIdAndGroupId(currUserId, groupId);

                if (groupApplyLog.isPresent()) {
                    if (groupApplyLog.get().getStatus() == 1) {
                        galRepos.delete(groupApplyLog.get());

                        Optional<MemberGroup> memberGroup = memberGroupRepos.findByMemberIdAndGroupId(currUserId, groupId);

                        memberGroupRepos.delete(memberGroup.get());

                        result.setCode(200);
                        result.setMessage("GROUP LEAVE SUCCESS");

                    } else {
                        throw new CustomException(Code.C565);
                    }

                } else {
                    throw new CustomException(Code.C300);
                }
            } else {
                throw new CustomException(Code.C300);
            }

        } else {
            throw new CustomException(Code.C510);
        }

        return result;
    }

    @Transactional
    public ApiResponse tossLeader(Long groupId, LeaderTossReqDTO leaderTossReqDTO) {
        ApiResponse result = new ApiResponse();

        Optional<Group> checkGroup = groupRepos.findById(groupId);

        if (checkGroup.isPresent()) {
            Group group = checkGroup.get();

            Long currUserId = authUtil.memberAuth();

            Optional<Member> checkCurrUser = memberRepos.findById(currUserId);

            if (checkCurrUser.isPresent()) {
                Member currUser = checkCurrUser.get();

                if (currUser.getId() == group.getLeader().getId()) {
                    Member member = memberRepos.findByNickName(leaderTossReqDTO.getNickName());

                    // 현재 넘겨받는 사람이 리더인지 아닌지 봐야한다.
                    Optional<MemberGroup> leaderGroup = memberGroupRepos.findByGroupIdAndMemberId(groupId, currUserId);
                    Optional<MemberGroup> memberGroup = memberGroupRepos.findByGroupIdAndMemberId(groupId, member.getId());

                    if (memberGroup.isPresent() && leaderGroup.isPresent()) {
                        group.setLeader(member);

                        leaderGroup.get().setGroupRole(GroupRole.MEMBER);
                        memberGroup.get().setGroupRole(GroupRole.LEADER);

                        groupRepos.save(group);
                        memberGroupRepos.save(memberGroup.get());

                        result.setCode(200);
                        result.setMessage("GROUP LEADER CHANGE SUCCESS");
                    } else {
                        throw new CustomException(Code.C565);
                    }

                } else {
                    throw new CustomException(Code.C565);
                }

            } else {
                throw new CustomException(Code.C565);
            }

        } else {
            throw new CustomException(Code.C510);
        }

        return result;
    }

    public ApiResponse kickGroup(Long groupId, GroupKickReqDTO groupKickReqDTO) {
        ApiResponse result = new ApiResponse();

        Optional<Group> checkGroup = groupRepos.findById(groupId);

        if (checkGroup.isPresent()) {
            Group group = checkGroup.get();
            Long currUserId = authUtil.memberAuth();

            if (currUserId == checkGroup.get().getLeader().getId()) {
                Member member = memberRepos.findByNickName(groupKickReqDTO.getNickName());

                Optional<GroupApplyLog> groupApplyLog = galRepos.findByMemberIdAndGroupId(member.getId(), groupId);
                Optional<MemberGroup> memberGroup = memberGroupRepos.findByMemberIdAndGroupId(member.getId(), groupId);

                if (groupApplyLog.isPresent() && memberGroup.isPresent()) {
                    groupApplyLog.get().setStatus(2);
                    groupApplyLog.get().setUpdate_date(new Date());

                    memberGroupRepos.delete(memberGroup.get());

                    result.setCode(200);
                    result.setMessage("GROUP MEMBER KICK SUCCESS");
                } else {
                    throw new CustomException(Code.C520);
                }

            } else {
                throw new CustomException(Code.C565);
            }
        } else {
            throw new CustomException(Code.C510);
        }

        return result;
    }

    public ApiResponse updateForm(Long groupId, HttpServletRequest request) {
        ApiResponse result = new ApiResponse();

        Group group = checkGroup(groupId);

        Long currUserId = authUtil.memberAuth();

        Optional<Member> checkCurrUser = memberRepos.findById(currUserId);

        if (checkCurrUser.isPresent()) {
            Member currUser = checkCurrUser.get();

            if (currUser.getId() == group.getLeader().getId()) {

                result.setResponseData("group", UpdateFormResDTO.builder()
                        .id(group.getId())
                        .name(group.getGroupName())
                        .description(group.getGroupInfo().getDescription())
                        .maxMember(group.getGroupInfo().getMaxMember())
                        .ctg(group.getCategory().stream().map(x -> x.getCategory().getName().toString()).collect(Collectors.toList()))
                        .display(group.getSecret())
                        .imgLink(group.getGroupInfo().getImageLink())
                        .build());

                result.setCode(200);
                result.setMessage("UPDATE-FORM SUCCESS");
            } else {
                throw new CustomException(Code.C565);
            }
        } else {
            throw new CustomException(Code.C503);
        }

        return result;
    }

    public Group checkGroup(Long groupId) {
        Group group;

        try {
            group = groupRepos.findById(groupId).get();
        } catch (Exception e) {
            throw new CustomException(Code.C510);
        }

        return group;
    }

}
