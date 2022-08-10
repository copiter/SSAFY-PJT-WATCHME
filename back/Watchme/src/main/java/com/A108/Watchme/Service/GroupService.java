package com.A108.Watchme.Service;

import com.A108.Watchme.DTO.*;
import com.A108.Watchme.DTO.Sprint.SprintGetResDTO;
import com.A108.Watchme.DTO.group.*;
import com.A108.Watchme.DTO.group.getGroup.*;
import com.A108.Watchme.DTO.group.getGroupList.GroupListResDTO;
import com.A108.Watchme.DTO.group.getGroupList.SprintDTO;
import com.A108.Watchme.Http.ApiResponse;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;

    private final CategoryRepository categoryRepository;
    private final GroupCategoryRepository groupCategoryRepository;
    private final GroupInfoRepository groupInfoRepos;
    private final GroupApplyLogRegistory groupApplyLogRegistory;
    private final PenaltyLogRegistory penaltyLogRegistory;
    private final MemberRepository memberRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final MRLRepository mrlRepository;
    private final SprintRepository sprintRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final S3Uploader s3Uploader;

    public ApiResponse getGroupList(String ctgName, String keyword, Integer page, Integer active, HttpServletRequest request) {
        ApiResponse result = new ApiResponse();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");

        List<Group> groupList = new LinkedList<>();

        if (page == null) {
            page = 1;
        }
        PageRequest pageRequest = PageRequest.of(page - 1, 10);

        if (active == null) {
            active = 0;
        }

        if (ctgName != null) {
            Category category = categoryRepository.findByName(CategoryList.valueOf(ctgName));

            if (keyword == null) {
                switch (active) {
                    case 0:
                        groupList = groupRepository.findAllByCategory_category(category, pageRequest).stream().collect(Collectors.toList());
                        break;
                    case 1:
                    case 2:
                        groupList = groupRepository.findAllByCategory_categoryAndDisplay(category, active, pageRequest).stream().collect(Collectors.toList());
                }
            } else {
                switch (active) {
                    case 0:
                        groupList = groupRepository.findAllByCategory_categoryAndGroupNameContaining(category, keyword, pageRequest).stream().collect(Collectors.toList());
                        break;
                    case 1:
                    case 2:
                        groupList = groupRepository.findAllByCategory_categoryAndDisplayAndGroupNameContaining(category, active, keyword, pageRequest).stream().collect(Collectors.toList());
                }
            }

        } else {
            if (keyword == null) {
                switch (active) {
                    case 0:
                        groupList = groupRepository.findAllByOrderByViewDesc(pageRequest).stream().collect(Collectors.toList());
                        break;
                    case 1:
                    case 2:
                        groupList = groupRepository.findAllByDisplayOrderByViewDesc(active, pageRequest).stream().collect(Collectors.toList());
                }
            } else {
                switch (active) {
                    case 0:
                        groupList = groupRepository.findAllByGroupNameContaining(keyword, pageRequest).stream().collect(Collectors.toList());
                        break;
                    case 1:
                    case 2:
                        groupList = groupRepository.findAllByGroupNameContainingAndDisplay(keyword, active, pageRequest).stream().collect(Collectors.toList());
                }

            }

        }

        List<GroupListResDTO> getGroupList = new LinkedList<>();

        for (Group g : groupList) {
            // endAt이 null인 (즉, 진행중인) sprint(들)을 collect
            List<Sprint> sprint = g.getSprints().stream().filter(x -> x.getSprintInfo().getEndAt().after(new Date())).collect(Collectors.toList());

            if (!sprint.isEmpty()) {
                Sprint currSprint = sprint.get(0);
                getGroupList.add(GroupListResDTO.builder()
                        .id(g.getId())
                        .name(g.getGroupName())
                        .description(g.getGroupInfo().getDescription())
                        .currMember(g.getGroupInfo().getCurrMember())
                        .maxMember(g.getGroupInfo().getMaxMember())
                        .ctg(g.getCategory().stream().map(x -> x.getCategory().getName().toString()).collect(Collectors.toList()))
                        .imgLink(g.getGroupInfo().getImageLink())
                        .createdAt(format.format(g.getCreatedAt()))
                        .display(g.getDisplay())
                        .view(g.getView())
                        .sprint(
                                SprintDTO.builder()
                                        .name(currSprint.getName())
                                        .description(currSprint.getSprintInfo().getDescription())
                                        .startAt(format.format(currSprint.getSprintInfo().getStartAt()))
                                        .endAt(format.format(currSprint.getSprintInfo().getEndAt()))
                                        .build()
                        )
                        .build()
                );
            } else {
                getGroupList.add(GroupListResDTO.builder()
                        .id(g.getId())
                        .name(g.getGroupName())
                        .description(g.getGroupInfo().getDescription())
                        .currMember(g.getGroupInfo().getCurrMember())
                        .maxMember(g.getGroupInfo().getMaxMember())
                        .ctg(g.getCategory().stream().map(x -> x.getCategory().getName().toString()).collect(Collectors.toList()))
                        .imgLink(g.getGroupInfo().getImageLink())
                        .createdAt(format.format(g.getCreatedAt()))
                        .display(g.getDisplay())
                        .view(g.getView())
                        .build()
                );
            }

        }

        result.setResponseData("groups", getGroupList);


        result.setMessage("GETROOMS SUCCESS");
        result.setCode(200);
        return result;
    }

    public ApiResponse getGroup(Long groupId, String pwd) {
        ApiResponse result = new ApiResponse();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");

        Optional<Group> check = groupRepository.findById(groupId);

        // 그룹 존재여부 체크
        if (check.isPresent()) {

            Group group = check.get();

            // password 체크
            if ((group.getGroupInfo().getPwd() == null && pwd == null) || bCryptPasswordEncoder.matches(pwd, group.getGroupInfo().getPwd())) {

                //group
                result.setResponseData("group", GroupResDTO.builder()
                        .name(group.getGroupName())
                        .description(group.getGroupInfo().getDescription())
                        .currMember(group.getGroupInfo().getCurrMember())
                        .maxMember(group.getGroupInfo().getMaxMember())
                        .ctg(group.getCategory().stream().map(x -> x.getCategory().getName().toString()).collect(Collectors.toList()))
                        .imgLink(group.getGroupInfo().getImageLink())
                        .createAt(format.format(group.getCreatedAt()))
                        .display(group.getDisplay())
                        .view(group.getView())
                        .build());


                // TODO : sprints 석인님이 작업하신 것으로 변경해야 됨


                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                Long memberId = Long.parseLong(((UserDetails) authentication.getPrincipal()).getUsername());

                List<SprintResDTO> sprintResDTOList = new LinkedList<>();

                List<Sprint> sprintList = sprintRepository.findAllByGroupId(groupId);

                for (Sprint sprint : sprintList) {
                    Optional<Integer> sum = mrlRepository.getSprintData(sprint.getRoom().getId());
                    Optional<MemberRoomLog> memberRoomLog = mrlRepository.findTopByRoomIdOrderByStudyTimeDesc(sprint.getRoom().getId());

                    int sumTime = 0;
                    if (sum.isPresent()) {
                        sumTime = sum.get();
                    }
                    String nickName = sprint.getGroup().getLeader().getNickName();
                    Integer kingTime = 0;
                    Integer count = 0;

                    if (memberRoomLog.isPresent()) {
                        nickName = memberRoomLog.get().getMember().getNickName();
                        kingTime = memberRoomLog.get().getStudyTime();
                        count = penaltyLogRegistory.countByMemberIdAndRoomId(memberRoomLog.get().getMember().getId(), sprint.getRoom().getId());
                    }

                    int sumPenalty = penaltyLogRegistory.countByRoomId(sprint.getRoom().getId());

                    SprintResDTO sprintResDTO = new SprintResDTO().builder()
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
                            .build();
                    sprintResDTOList.add(sprintResDTO);

                }

                result.setResponseData("sprints", sprintResDTOList);


                // leader
                Member leader = group.getLeader();

                result.setResponseData("leader", GroupMemberResDTO.builder()
                        .nickName(leader.getNickName())
                        .imgLink(leader.getMemberInfo().getImageLink())
                        .role(GroupRole.LEADER.ordinal())
                        .build());


                // 접속자 체크
                // 비로그인
                if (authentication.getPrincipal().equals("anonymousUser")) {

                    result.setResponseData("myData", MyDataResDTO.builder()
                            .role(GroupRole.ANONYMOUS.ordinal())
                            .build());
                    result.setCode(200);
                    result.setMessage("GET GROUP SUCCESS");
                    return result;
                }

                // 로그인
                String currUserId = ((UserDetails) authentication.getPrincipal()).getUsername();

                Optional<Member> checkMember = memberRepository.findById(Long.parseLong(currUserId));
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
                                .role(m.getNickName().equals(leader.getNickName()) ? GroupRole.LEADER.ordinal() : GroupRole.MEMBER.ordinal())
                                .build()
                        );
                    }

                    result.setResponseData("members", members);


                    // myData
                    // myData.studyTime
                    List<Long> roomIdList = group.getSprints().stream().map(x -> x.getRoom().getId()).collect(Collectors.toList());

                    List<MemberRoomLog> memberRoomLogList = mrlRepository.findByMemberIdAndRoomIdIn(currMember.getId(), roomIdList);
                    int studyTime = 0;

                    for (MemberRoomLog mrl :
                            memberRoomLogList) {
                        studyTime += mrl.getStudyTime();
                    }

                    // myData.penalty
                    List<Integer> penalty = new ArrayList<>(RuleName.values().length);
                    List<PenaltyLog> penaltyLogList = penaltyLogRegistory.findAllByMemberIdAndSprintIn(currMember.getId(), group.getSprints());

                    for (RuleName rule :
                            RuleName.values()) {
                        penalty.add(rule.ordinal(), (int) (long) penaltyLogList.stream().filter(x -> x.getRule().getRuleName().ordinal() == rule.ordinal()).count());
                    }

                    MemberGroup currMemberGroup = memberGroupRepository.findByMemberIdAndGroupId(currMember.getId(), groupId).get();

                    result.setResponseData("myData", MyDataResDTO.builder()
                            .role(currMember.getEmail().equals(leader.getEmail()) ? GroupRole.LEADER.ordinal() : GroupRole.MEMBER.ordinal())
                            .studyTime(studyTime)
                            .penalty(penalty)
                            .joinDate(format.format(currMemberGroup.getCreatedAt()))
                            .build()
                    );

                    //groupdata
                    List<MemberRoomLog> groupRoomLogList = mrlRepository.findByRoomIdIn(roomIdList);
                    GroupDataResDTO groupDataResDTO;

                    int sumTime = 0;
                    for (MemberRoomLog mrl :
                            groupRoomLogList) {
                        sumTime += mrl.getStudyTime();
                    }

                    if (group.getLeader().getId().equals(currMember.getId())) {
                        int assignee = (int) groupApplyLogRegistory.countByGroupIdAndStatus(groupId, 0);
                        groupDataResDTO = GroupDataResDTO.builder()
                                .sumTime(sumTime)
                                .assignee(assignee)
                                .build();
                    } else {
                        groupDataResDTO = GroupDataResDTO.builder()
                                .sumTime(sumTime)
                                .build();
                    }

                    result.setResponseData("groupData",groupDataResDTO);

                    result.setCode(200);
                    result.setMessage("GET GROUP SUCCESS");

                } else {
                    result.setResponseData("myData", MyDataResDTO.builder()
                            .role(GroupRole.ANONYMOUS.ordinal())
                            .build());
                    result.setCode(200);
                    result.setMessage("GET GROUP SUCCESS");
                    return result;
                }

            } else {
                result.setCode(307);
                result.setMessage("Wrong Password");

            }

        } else {
            result.setCode(400);
            result.setMessage("no such group");
        }
        return result;
    }


    public ApiResponse createGroup(GroupCreateReqDTO groupCreateReqDTO, MultipartFile image, HttpServletRequest request) {
        ApiResponse result = new ApiResponse();

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Optional<Member> currUser = memberRepository.findById(Long.parseLong(((UserDetails) (authentication.getPrincipal())).getUsername()));

            if (currUser.isPresent()) {

                String url = "https://popoimages.s3.ap-northeast-2.amazonaws.com/StudyRoom.jpg";

                try {
                    url = s3Uploader.upload(image, "Watchme"); // TODO : groupImg랑 roomImg를 다른 디렉토리에 저장해야될거같은데?
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // TODO : GroupBuilder
                // 1.group 기본 저장
                Group newGroup = Group.builder()
                        .groupName(groupCreateReqDTO.getName())
                        .leader(currUser.get())
                        .status(Status.YES)
                        .view(0)
                        .build();

                groupRepository.save(newGroup);

                GroupInfo newGroupInfo = groupInfoRepos.save(GroupInfo.builder()
                        .group(newGroup)
                        .imageLink(url)
                        .description(groupCreateReqDTO.getDescription())
                        .currMember(1)
                        .maxMember(Integer.parseInt(groupCreateReqDTO.getMaxMember()))
                        .pwd(groupCreateReqDTO.getPwd())
                        .build());


                // 2.MemberGroup save
                memberGroupRepository.save(MemberGroup.builder()
                        .group(newGroupInfo.getGroup())
                        .member(newGroupInfo.getGroup().getLeader())
                        .createdAt(new Date())
                        .groupRole(GroupRole.LEADER)
                        .build());


                // 3.GroupCategory

                for (String ctg :
                        groupCreateReqDTO.getCtg()) {
                    groupCategoryRepository.save(GroupCategory.builder()
                            .category(categoryRepository.findByName(CategoryList.valueOf(ctg)))
                            .group(newGroupInfo.getGroup())
                            .build());
                }


                result.setCode(200);
                result.setMessage("SUCCESS ADD&JOIN ROOM");
                result.setResponseData("groupId", newGroupInfo.getGroup().getId());

            }

        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
        }

        return result;
    }

    public ApiResponse updateGroup(Long groupId, GroupUpdateReqDTO groupUpdateReqDTO, MultipartFile image, HttpServletRequest request) {
        ApiResponse result = new ApiResponse();

        try {
            Optional<Group> check = groupRepository.findById(groupId);

            if (check.isPresent()) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                Optional<Member> currUser = memberRepository.findById(Long.parseLong(((UserDetails) (authentication.getPrincipal())).getUsername()));


                Group group = check.get();

                String url = group.getGroupInfo().getImageLink();

                try {
                    url = s3Uploader.upload(image, "Watchme"); // TODO : groupImg랑 roomImg를 다른 디렉토리에 저장해야될거같은데?
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // TODO : Update Group
                // 그룹 카테고리 삭제
                List<GroupCategory> groupCategoryList = groupCategoryRepository.findAllByGroupId(groupId);
                groupCategoryRepository.deleteAllInBatch(groupCategoryList);

                List<GroupCategory> categoryList = new LinkedList<>();

                for (String ctg :
                        groupUpdateReqDTO.getCtg()) {
                    categoryList.add(GroupCategory.builder()
                            .category(categoryRepository.findByName(CategoryList.valueOf(ctg)))
                            .group(group)
                            .build());
                }

                group.setGroupName(groupUpdateReqDTO.getName());
                group.setCategory(categoryList);
                group.setDisplay(groupUpdateReqDTO.getDisplay());

                GroupInfo groupInfo = groupInfoRepos.findById(groupId).get();
                groupInfo.setDescription(groupUpdateReqDTO.getDescription());
                groupInfo.setMaxMember(Integer.parseInt(groupUpdateReqDTO.getMaxMember()));
                groupInfo.setPwd(groupUpdateReqDTO.getPwd());


                groupRepository.save(group);
                groupInfoRepos.save(groupInfo);

                result.setCode(200);
                result.setMessage("SUCCESS GROUP UPDATE");

            } else {
                throw new Exception("no such group");
            }


        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
        }

        return result;
    }

    public ApiResponse deleteGroup(Long groupId) {
        ApiResponse result = new ApiResponse();

        Optional<Group> check = groupRepository.findById(groupId);

        if (check.isPresent()) {
            check.get().setStatus(Status.NO);

            result.setCode(200);
            result.setMessage("GROUP DELETE SUCCESS");
        } else {
            result.setCode(400);
            result.setMessage("NO SUCH GROUP");
        }

        return result;
    }

    public ApiResponse getApplyList(Long groupId) {
        ApiResponse result = new ApiResponse();
        Optional<Group> group = groupRepository.findById(groupId);

        if (group != null) {
            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (!authentication.getAuthorities().toString().equals("[ROLE_ANONYMOUS]")) {

                    UserDetails currUser = (UserDetails) authentication.getPrincipal();
                    if (currUser.getUsername().equals(group.get().getLeader().getEmail())) {
                        // 그룹 리더임
                        List<GroupApplyLog> applyLogs = groupApplyLogRegistory.findAllByGroupId(groupId);
                        List<GroupApplyDTO> getApplys = new LinkedList<>();

                        for (GroupApplyLog applyLog : applyLogs) {
                            Member member = applyLog.getMember();

                            Integer penaltyScore = penaltyLogRegistory.findAllByMemberId(member.getId()).size();

                            getApplys.add(new GroupApplyDTO().builder()
                                    .email(member.getEmail()).nickName(member.getNickName())
                                    .imgLink(member.getMemberInfo().getImageLink())
                                    .studyTime(member.getMemberInfo().getStudyTime())
                                    .penaltyScore(penaltyScore)
                                    .build()
                            );
                        }
                        result.setResponseData("appliers", getApplys);
                        result.setCode(200);
                        result.setMessage("GROUP APPLY LIST SUCCESS");
                    } else {
                        // 그룹 리더가 아님
                        result.setCode(400);
                        result.setMessage("ONLY GROUP LEADER CAN GET APPLYES");
                    }
                }
            } catch (Exception e) {
                result.setCode(400);
                result.setMessage("LOGIN USER ONLY");
            }
        } else {
            result.setCode(400);
            result.setMessage("CAN'T FIND GROUP BY GROUP ID");
        }

        return result;
    }

    public ApiResponse apply(Long groupId) {
        ApiResponse result = new ApiResponse();
        Optional<Group> group = groupRepository.findById(groupId);

        if (group != null) {
            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                UserDetails currUser = (UserDetails) authentication.getPrincipal();

                Member member = memberRepository.findByEmail(currUser.getUsername());

                Optional<GroupApplyLog> groupApplyLog = groupApplyLogRegistory.findByMemberIdAndGroupId(member.getId(), groupId);

                groupApplyLogRegistory.save(GroupApplyLog.builder()
                        .member(member)
                        .group(group.get())
                        .apply_date(new Date())
                        .status(0)
                        .build()
                );
                result.setCode(200);
                result.setMessage("GROUP JOIN APPLY SUCCESS");

            } catch (Exception e) {
                result.setCode(400);
                result.setMessage("LOGIN USER ONLY");
            }
        } else {
            result.setCode(400);
            result.setMessage("CAN'T FIND GROUP BY GROUP ID");
        }

        return result;
    }

    public ApiResponse cancelApply(Long groupId) {
        ApiResponse result = new ApiResponse();
        Optional<Group> group = groupRepository.findById(groupId);

        if (group != null) {
            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                UserDetails currUser = (UserDetails) authentication.getPrincipal();
                Member member = memberRepository.findByEmail(currUser.getUsername());

                Optional<GroupApplyLog> groupApplyLog = groupApplyLogRegistory.findByMemberIdAndGroupId(member.getId(), groupId);

                if (groupApplyLog != null) {
                    groupApplyLog.get().setStatus(4);
                    groupApplyLog.get().setUpdate_date(new Date());

                    result.setCode(200);
                    result.setMessage("GROUP APPLY CANCLE SUCCESS");
                } else {
                    result.setCode(400);
                    result.setMessage("THERE IS NO APPLY");
                }
            } catch (Exception e) {
                result.setCode(400);
                result.setMessage("LOGIN USER ONLY");
            }
        } else {
            result.setCode(400);
            result.setMessage("CAN'T FIND GROUP BY GROUP ID");
        }
        return result;
    }

    public ApiResponse acceptApply(Long groupId, AcceptApplyReqDTO acceptApplyReqDTO) {
        ApiResponse result = new ApiResponse();
        Optional<Group> group = groupRepository.findById(groupId);

        if (group != null) {
            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

                UserDetails currUser = (UserDetails) authentication.getPrincipal();

                if (currUser.getUsername().equals(group.get().getLeader().getEmail())) {

                    Member member = memberRepository.findByNickName(acceptApplyReqDTO.getNickName());
                    Optional<GroupApplyLog> groupApplyLog = groupApplyLogRegistory.findByMemberIdAndGroupId(member.getId(), groupId);

                    if (groupApplyLog != null) {
                        groupApplyLog.get().setStatus(1);
                        groupApplyLog.get().setUpdate_date(new Date());

                        memberGroupRepository.save(MemberGroup.builder()
                                .member(member)
                                .group(group.get())
                                .createdAt(new Date())
                                .build()
                        );

                        result.setCode(200);
                        result.setMessage("GROUP APPLY ACCEPT SUCCESS");
                    } else {
                        result.setCode(400);
                        result.setMessage("THERE IS NO APPLY");
                    }

                } else {
                    // 그룹 리더가 아님
                    result.setCode(400);
                    result.setMessage("ONLY GROUP LEADER DECLINE APPLYES");
                }
            } catch (Exception e) {
                result.setCode(400);
                result.setMessage("LOGIN USER ONLY");
            }
        } else {
            result.setCode(400);
            result.setMessage("CAN'T FIND GROUP BY GROUP ID");
        }

        return result;
    }

    public ApiResponse declineApply(Long groupId, DeclineApplyReqDTO declineApplyReqDTO) {
        ApiResponse result = new ApiResponse();
        Optional<Group> group = groupRepository.findById(groupId);

        if (group != null) {
            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (!authentication.getAuthorities().toString().equals("[ROLE_ANONYMOUS]")) {
                    UserDetails currUser = (UserDetails) authentication.getPrincipal();


                    if (currUser.getUsername().equals(group.get().getLeader().getEmail())) {

                        Member member = memberRepository.findByNickName(declineApplyReqDTO.getNickName());

                        Optional<GroupApplyLog> groupApplyLog = groupApplyLogRegistory.findByMemberIdAndGroupId(member.getId(), groupId);
                        if (groupApplyLog != null) {
                            groupApplyLog.get().setStatus(2);
                            groupApplyLog.get().setUpdate_date(new Date());

                            result.setCode(200);
                            result.setMessage("GROUP APPLY ACCEPT SUCCESS");
                        } else {
                            result.setCode(400);
                            result.setMessage("THERE IS NO APPLY");
                        }

                    } else {
                        // 그룹 리더가 아님
                        result.setCode(400);
                        result.setMessage("ONLY GROUP LEADER DECLINE APPLYES");
                    }
                }
            } catch (Exception e) {
                result.setCode(400);
                result.setMessage("LOGIN USER ONLY");
            }
        } else {
            result.setCode(400);
            result.setMessage("CAN'T FIND GROUP BY GROUP ID");
        }

        return result;
    }

    public ApiResponse leaveGroup(Long groupId) {
        ApiResponse result = new ApiResponse();
        Optional<Group> group = groupRepository.findById(groupId);

        if (group != null) {
            try {

                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (!authentication.getAuthorities().toString().equals("[ROLE_ANONYMOUS]")) {
                    UserDetails currUser = (UserDetails) authentication.getPrincipal();
                    Member member = memberRepository.findByEmail(currUser.getUsername());

                    Optional<GroupApplyLog> groupApplyLog = groupApplyLogRegistory.findByMemberIdAndGroupId(member.getId(), groupId);


                    if (groupApplyLog != null) {
                        groupApplyLog.get().setStatus(5);
                        groupApplyLog.get().setUpdate_date(new Date());

                        Optional<MemberGroup> memberGroup = memberGroupRepository.findByMemberIdAndGroupId(member.getId(), groupId);
                        memberGroupRepository.delete(memberGroup.get());

                        result.setCode(200);
                        result.setMessage("GROUP LEAVE SUCCESS");

                    } else {
                        result.setCode(400);
                        result.setMessage("THERE IS NO APPLY");
                    }
                }
            } catch (Exception e) {
                result.setCode(400);
                result.setMessage("LOGIN USER ONLY");
            }
        } else {
            result.setCode(400);
            result.setMessage("CAN'T FIND GROUP BY GROUP ID");
        }

        return result;
    }

    public ApiResponse tossLeader(Long groupId, LeaderTossReqDTO leaderTossReqDTO) {
        ApiResponse result = new ApiResponse();
        Optional<Group> group = groupRepository.findById(groupId);

        if (group != null) {
            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (!authentication.getAuthorities().toString().equals("[ROLE_ANONYMOUS]")) {

                    UserDetails currUser = (UserDetails) authentication.getPrincipal();

                    if (currUser.getUsername().equals(group.get().getLeader().getEmail())) {

                        Member member = memberRepository.findByNickName(leaderTossReqDTO.getNickName());

                        // 방장 바뀐 것도 로그로 남겨야 하는가?
                        group.get().setLeader(member);

                        result.setCode(200);
                        result.setMessage("GROUP LEADER CHANGE SUCCESS");


                    } else {
                        // 그룹 리더가 아님
                        result.setCode(400);
                        result.setMessage("ONLY GROUP LEADER GET APPLYES");
                    }
                }
            } catch (Exception e) {
                result.setCode(400);
                result.setMessage("LOGIN USER ONLY");
            }
        } else {
            result.setCode(400);
            result.setMessage("CAN'T FIND GROUP BY GROUP ID");
        }

        return result;
    }

    public ApiResponse kickGroup(Long groupId, GroupKickReqDTO groupKickReqDTO) {
        ApiResponse result = new ApiResponse();
        Optional<Group> group = groupRepository.findById(groupId);

        if (group != null) {
            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (!authentication.getAuthorities().toString().equals("[ROLE_ANONYMOUS]")) {

                    UserDetails currUser = (UserDetails) authentication.getPrincipal();

                    if (currUser.getUsername().equals(group.get().getLeader().getEmail())) {
                        Member member = memberRepository.findByNickName(groupKickReqDTO.getNickName());

                        Optional<GroupApplyLog> groupApplyLog = groupApplyLogRegistory.findByMemberIdAndGroupId(member.getId(), groupId);

                        Optional<MemberGroup> memberGroup = memberGroupRepository.findByMemberIdAndGroupId(member.getId(), groupId);

                        if (groupApplyLog != null && memberGroup != null) {
                            groupApplyLog.get().setStatus(5);
                            groupApplyLog.get().setUpdate_date(new Date());

                            memberGroupRepository.delete(memberGroup.get());

                            result.setCode(200);
                            result.setMessage("GROUP APPLY ACCEPT SUCCESS");
                        } else {
                            result.setCode(400);
                            result.setMessage("CAN'T FIND ON DB");
                        }

                    } else {
                        // 그룹 리더가 아님
                        result.setCode(400);
                        result.setMessage("ONLY GROUP LEADER GET APPLYES");
                    }
                }
            } catch (Exception e) {
                result.setCode(400);
                result.setMessage("LOGIN USER ONLY");
            }
        } else {
            result.setCode(400);
            result.setMessage("CAN'T FIND GROUP BY GROUP ID");
        }

        return result;
    }

    public ApiResponse updateForm(Long groupId, HttpServletRequest request) {
        ApiResponse result = new ApiResponse();

        Optional<Group> check = groupRepository.findById(groupId);

        if (check.isPresent()) {
            Group group = check.get();

            result.setResponseData("group", UpdateFormResDTO.builder()
                    .id(group.getId())
                    .name(group.getGroupName())
                    .description(group.getGroupInfo().getDescription())
                    .maxMember(group.getGroupInfo().getMaxMember())
                    .ctg(group.getCategory().stream().map(x -> x.getCategory().getName().toString()).collect(Collectors.toList()))
                    .pwd(group.getGroupInfo().getPwd())
                    .display(group.getDisplay())
                    .imgLink(group.getGroupInfo().getImageLink())
                    .build());

            result.setCode(200);
            result.setMessage("UPDATE-FORM SUCCESS");
        }

        return result;
    }
}
