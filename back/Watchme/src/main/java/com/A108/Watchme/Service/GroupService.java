package com.A108.Watchme.Service;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
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
    @Deprecated(forRemoval = true)
    private final GroupApplyLogRegistory groupApplyLogRegistory;
    private final GroupApplyLogRegistory groupApplyLogRepository;
    private final PenaltyLogRegistory penaltyLogRegistory;
    private final MemberRepository memberRepository;
    private final MemberGroupRepository memberGroupRepository;
    private final MRLRepository mrlRepository;
    private final SprintRepository sprintRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final S3Uploader s3Uploader;

    public ApiResponse getGroupList(String ctgName, String keyword, Integer page, HttpServletRequest request) {
        ApiResponse result = new ApiResponse();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");

        List<Group> groupList = new LinkedList<>();

        if (page == null) {
            page = 1;
        }
        PageRequest pageRequest = PageRequest.of(page - 1, 10);

        if (ctgName != null) {
            Category category = categoryRepository.findByName(CategoryList.valueOf(ctgName));

            if (keyword == null) {
                groupList = groupRepository.findAllByCategory_category(category, pageRequest).stream().collect(Collectors.toList());

            } else {
                groupList = groupRepository.findAllByCategory_categoryAndGroupNameContaining(category, keyword, pageRequest).stream().collect(Collectors.toList());
            }

        } else {
            if (keyword == null) {
                groupList = groupRepository.findAllByOrderByViewDesc(pageRequest).stream().collect(Collectors.toList());

            } else {
                groupList = groupRepository.findAllByGroupNameContaining(keyword, pageRequest).stream().collect(Collectors.toList());
            }

        }

        if (groupList.isEmpty()) {
            throw new CustomException(Code.C520);
        }

        List<GroupListResDTO> getGroupList = new LinkedList<>();

        if (!groupList.isEmpty()) {
            for (Group g : groupList) {
                // endAt이 현재보다 이후인 (즉, 진행중인) sprint(들)을 collect
                List<Sprint> sprint = g.getSprints().stream().filter(x -> x.getSprintInfo().getStartAt().after(new Date())).collect(Collectors.toList());

                // 첫번째 sprint를 반환 : 프론트 요구에 따라 배열로 전달할 수도 있겠다.
                Sprint currSprint;

                getGroupList.add(GroupListResDTO.builder()
                        .id(g.getId())
                        .name(g.getGroupName())
                        .description(g.getGroupInfo().getDescription())
                        .currMember(g.getGroupInfo().getCurrMember())
                        .maxMember(g.getGroupInfo().getMaxMember())
                        .ctg(g.getCategory().stream().map(x -> x.getCategory().getName().toString()).collect(Collectors.toList()))
                        .imgLink(g.getGroupInfo().getImageLink())
                        .createdAt(format.format(g.getCreatedAt()))
                        .secret(g.getDisplay() == 1 ? true : false)
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
        } else {
            result.setCode(400);
            result.setMessage("No group result");
        }

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


                // sprints
                List<SprintResDTO> sprintResDTOList = new LinkedList<>();

                List<Sprint> sprintList = sprintRepository.findAllByGroupId(groupId);

                for (Sprint sprint : sprintList) {
                    int sumTime = 0;
                    String nickName = sprint.getGroup().getLeader().getNickName();
                    Integer kingTime = 0;
                    Integer count = 0;

                    Optional<Integer> sum = mrlRepository.getSprintData(sprint.getRoom().getId());
                    if (sum.isPresent()) {
                        sumTime = sum.get();
                    } else {

                    }

                    // TODO : repository 이게 맞나..?
                    Optional<MemberRoomLog> checkMrl = mrlRepository.findTopByRoomIdOrderByStudyTimeDesc(sprint.getRoom().getId());
                    if (checkMrl.isPresent()) {
                        MemberRoomLog memberRoomLog = checkMrl.get();

                        nickName = memberRoomLog.getMember().getNickName();
                        kingTime = memberRoomLog.getStudyTime();
                        count = penaltyLogRegistory.countByMemberIdAndRoomId(memberRoomLog.getMember().getId(), sprint.getRoom().getId());
                    }

                    int sumPenalty = penaltyLogRegistory.countByRoomId(sprint.getRoom().getId());


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
                if (authentication.getPrincipal().equals("anonymousUser")) {

                    result.setResponseData("myData", MyDataResDTO.builder()
                            .role(GroupRole.ANONYMOUS.ordinal())
                            .build());

                    List<Long> roomIdList = group.getSprints().stream().map(x -> x.getRoom().getId()).collect(Collectors.toList());
                    List<MemberRoomLog> groupRoomLogList = mrlRepository.findByRoomIdIn(roomIdList);

                    int sumTime = 0;
                    for (MemberRoomLog mrl :
                            groupRoomLogList) {
                        sumTime += mrl.getStudyTime();
                    }

                    GroupDataResDTO groupDataResDTO = GroupDataResDTO.builder()
                            .sumTime(sumTime)
                            .build();

                    result.setResponseData("groupData", groupDataResDTO);

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
                                .role(m.getEmail().equals(leader.getEmail()) ? GroupRole.LEADER.ordinal() : GroupRole.MEMBER.ordinal())
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
                    List<Integer> penalty = new ArrayList<>(Mode.values().length);
                    List<PenaltyLog> penaltyLogList = penaltyLogRegistory.findAllByMemberIdAndRoomIn(currMember.getId(), group.getSprints().stream().map(x -> x.getRoom()).collect(Collectors.toList()));

                    for (PenaltyLog p :
                            penaltyLogList) {
                        System.out.println(p.getId());
                        System.out.println(p.getRoom().getId());
                        System.out.println(p.getMode().toString());
                        System.out.println("--------------");
                    }

                    for (Mode mode :
                            Mode.values()) {
                        penalty.add(mode.ordinal(), (int) (long) penaltyLogList.stream().filter(x -> x.getMode().ordinal() == mode.ordinal()).count());
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

                    result.setResponseData("groupData", groupDataResDTO);

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
                throw new CustomException(Code.C564);
            }

        } else {
            throw new CustomException(Code.C510);
        }
        return result;
    }

    @Transactional
    public ApiResponse createGroup(GroupCreateReqDTO groupCreateReqDTO, MultipartFile image, HttpServletRequest request) {
        ApiResponse result = new ApiResponse();

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Optional<Member> currUser = memberRepository.findById(Long.parseLong(((UserDetails) (authentication.getPrincipal())).getUsername()));

            if (currUser.isPresent()) {

                String url = "https://popoimages.s3.ap-northeast-2.amazonaws.com/StudyRoom.jpg";

                if (image != null) {
                    try {
                        url = s3Uploader.upload(image, "Watchme"); // TODO : groupImg랑 roomImg를 다른 디렉토리에 저장해야될거같은데?
                    } catch (Exception e) {
                        throw new CustomException(Code.C512);
                    }
                }

                try {
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
                            .maxMember(groupCreateReqDTO.getMaxMember())
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
                } catch (Exception e) {
                    throw new CustomException(Code.C521);
                }

            } else {
                throw new CustomException(Code.C501);
            }

        } catch (Exception e) {
            throw new CustomException(Code.C501);
        }

        return result;
    }

    @Transactional
    public ApiResponse updateGroup(Long groupId, GroupUpdateReqDTO groupUpdateReqDTO, MultipartFile image, HttpServletRequest request) {
        ApiResponse result = new ApiResponse();

        try {
            Optional<Group> check = groupRepository.findById(groupId);

            if (check.isPresent()) {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                Optional<Member> checkCurrUser = memberRepository.findById(Long.parseLong(((UserDetails) (authentication.getPrincipal())).getUsername()));

                Group group = check.get();

                if (checkCurrUser.isPresent()) {
                    Member currUser = checkCurrUser.get();

                    if (currUser.getId() != group.getLeader().getId()) {
                        throw new CustomException(Code.C565);
                    }

                    String url = group.getGroupInfo().getImageLink();

                    if (image != null) {
                        try {
                            url = s3Uploader.upload(image, "Watchme"); // TODO : groupImg랑 roomImg를 다른 디렉토리에 저장해야될거같은데?
                        } catch (Exception e) {
                            throw new CustomException(Code.C512);
                        }
                    }

                    // 그룹 카테고리 삭제
                    List<GroupCategory> groupCategoryList = groupCategoryRepository.findAllByGroupId(groupId);
                    groupCategoryRepository.deleteAllInBatch(groupCategoryList);

                    List<GroupCategory> categoryList = new LinkedList<>();

                    try {
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
                    } catch (Exception e) {
                        throw new CustomException(Code.C521);
                    }

                    result.setCode(200);
                    result.setMessage("SUCCESS GROUP UPDATE");
                } else {
                    throw new CustomException(Code.C503);
                }

            } else {
                throw new CustomException(Code.C510);
            }


        } catch (Exception e) {
            throw new CustomException(Code.C500);
        }

        return result;
    }

    public ApiResponse deleteGroup(Long groupId) {
        ApiResponse result = new ApiResponse();

        Optional<Group> check = groupRepository.findById(groupId);

        if (check.isPresent()) {
            Group group = check.get();

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication.getPrincipal().equals("anonymousUser")) {
                throw new CustomException(Code.C501);
            }

            Long currUserId = Long.parseLong(((UserDetails) authentication.getPrincipal()).getUsername());

            Optional<Member> currUser = memberRepository.findById(currUserId);

            if (currUser.isPresent() && group.getLeader().getId() == currUserId) {
                group.setStatus(Status.NO);

                result.setCode(200);
                result.setMessage("GROUP DELETE SUCCESS");

            } else {
                throw new CustomException(Code.C565);
            }

        } else {
            throw new CustomException(Code.C510);
        }

        return result;
    }

    public ApiResponse getApplyList(Long groupId) {
        ApiResponse result = new ApiResponse();

        Optional<Group> checkGroup = groupRepository.findById(groupId);

        if (checkGroup.isPresent()) {
            Group group = checkGroup.get();

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication.getPrincipal().equals("anonymousUser")) {
                throw new CustomException(Code.C501);
            }

            Long currUserId = Long.parseLong(((UserDetails) authentication.getPrincipal()).getUsername());
            Optional<Member> checkCurrUser = memberRepository.findById(currUserId);

            if (checkCurrUser.isPresent()) {
                Member currUser = checkCurrUser.get();

                if (currUser.getId() == group.getLeader().getId()) {
                    // 그룹 리더임
                    List<GroupApplyLog> applyLogs = groupApplyLogRepository.findAllByGroupId(groupId);

                    for (GroupApplyLog a :
                            applyLogs) {
                        System.out.println(a.getStatus());
                    }

                    applyLogs = applyLogs.stream().filter(x -> x.getStatus() == 0).collect(Collectors.toList());

                    List<GroupApplyDTO> getApplys = new LinkedList<>();

                    for (GroupApplyLog applyLog : applyLogs) {
                        Member member = applyLog.getMember();

                        //
                        List<Integer> penalty = new ArrayList<>(Mode.values().length);
                        List<PenaltyLog> penaltyLogList = penaltyLogRegistory.findAllByMemberId(member.getId());

                        if (penaltyLogList.size() != 0) {
                            for (Mode mode :
                                    Mode.values()) {
                                penalty.add(mode.ordinal(), (int) penaltyLogList.stream().filter(x -> x.getMode().ordinal() == mode.ordinal()).count());
                            }
                        }

                        //
                        getApplys.add(new GroupApplyDTO().builder()
                                .email(member.getEmail()).nickName(member.getNickName())
                                .imgLink(member.getMemberInfo().getImageLink())
                                .studyTime(member.getMemberInfo().getStudyTime())
                                .penalty(penalty)
                                .build()
                        );
                    }

                    List<Member> groupMembers = group.getMemberGroupList().stream().map(x -> x.getMember()).collect(Collectors.toList());


                    // members
                    List<GroupMemberResDTO> members = new LinkedList<>();

                    for (Member m : groupMembers) {

                        members.add(GroupMemberResDTO.builder()
                                .nickName(m.getNickName())
                                .build()
                        );
                    }


                    result.setResponseData("appliers", getApplys);
                    result.setResponseData("members", members);

                    result.setCode(200);
                    result.setMessage("GROUP APPLY LIST SUCCESS");

                } else {
                    throw new CustomException(Code.C565);
                }
            } else {
                throw new CustomException(Code.C503);
            }

        } else {
            result.setCode(400);
            result.setMessage("CAN'T FIND GROUP BY GROUP ID");
        }

        return result;
    }

    public ApiResponse apply(Long groupId) {
        ApiResponse result = new ApiResponse();

        Optional<Group> checkGroup = groupRepository.findById(groupId);

        if (checkGroup.isPresent()) {
            Group group = checkGroup.get();

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication.getPrincipal().equals("anonymousUser")) {
                throw new CustomException(Code.C501);
            }

            Long currUserId = Long.parseLong(((UserDetails) authentication.getPrincipal()).getUsername());
            Optional<Member> checkCurrUser = memberRepository.findById(currUserId);

            if (checkCurrUser.isPresent()) {
                Member currUser = checkCurrUser.get();

                Optional<GroupApplyLog> groupApplyLog = groupApplyLogRegistory.findByMemberIdAndGroupId(currUserId, groupId);
                List<Long> groupMembers = group.getMemberGroupList().stream().map(x -> x.getMember().getId()).collect(Collectors.toList());

                if (groupMembers.contains(currUserId)) {
                    throw new CustomException(Code.C509);
                } else {
                    if (groupApplyLog.isEmpty()) {
                        groupApplyLogRegistory.save(GroupApplyLog.builder()
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
                        } else {
                            throw new CustomException(Code.C567);
                        }
                    }
                }
            } else {
                throw new CustomException(Code.C503);
            }
        } else {
            throw new CustomException(Code.C510);
        }

        return result;
    }

    public ApiResponse cancelApply(Long groupId) {
        ApiResponse result = new ApiResponse();

        Optional<Group> group = groupRepository.findById(groupId);

        if (group.isPresent()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication.getPrincipal().equals("anonymousUser")) {
                throw new CustomException(Code.C501);
            }

            Long currUserId = Long.parseLong(((UserDetails) authentication.getPrincipal()).getUsername());

            Optional<Member> member = memberRepository.findById(currUserId);

            if (member.isPresent()) {
                Optional<GroupApplyLog> checkGroupApplyLog = groupApplyLogRegistory.findByMemberIdAndGroupId(currUserId, groupId);

                if (checkGroupApplyLog.isPresent()) {
                    // 로그에서 삭제
                    GroupApplyLog groupApplyLog = checkGroupApplyLog.get();

                    if (groupApplyLog.getStatus() == 0) {
                        groupApplyLogRegistory.delete(groupApplyLog);

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

        Optional<Group> checkGroup = groupRepository.findById(groupId);

        if (checkGroup.isPresent()) {
            Group group = checkGroup.get();

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication.getPrincipal().equals("anonymousUser")) {
                throw new CustomException(Code.C501);
            }

            Long currUserId = Long.parseLong(((UserDetails) authentication.getPrincipal()).getUsername());

            Optional<Member> checkCurrUser = memberRepository.findById(currUserId);

            if (checkCurrUser.isPresent()) {
                Member currUser = checkCurrUser.get();

                if (currUser.getId() == group.getLeader().getId()) {
                    Member applier = memberRepository.findByNickName(acceptApplyReqDTO.getNickName());

                    Optional<GroupApplyLog> groupApplyLog = groupApplyLogRegistory.findByMemberIdAndGroupId(applier.getId(), groupId);

                    if (groupApplyLog.isPresent()) {
                        groupApplyLog.get().setStatus(1);
                        groupApplyLog.get().setUpdate_date(new Date());

                        groupApplyLogRegistory.save(groupApplyLog.get());

                        memberGroupRepository.save(MemberGroup.builder()
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

        Optional<Group> checkGroup = groupRepository.findById(groupId);

        if (checkGroup.isPresent()) {
            Group group = checkGroup.get();

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication.getPrincipal().equals("anonymousUser")) {
                throw new CustomException(Code.C501);
            }

            Long currUserId = Long.parseLong(((UserDetails) authentication.getPrincipal()).getUsername());
            Optional<Member> checkCurrUser = memberRepository.findById(currUserId);

            if (checkCurrUser.isPresent()) {
                Member currUser = checkCurrUser.get();

                if (currUser.getId() == group.getLeader().getId()) {
                    Member member = memberRepository.findByNickName(declineApplyReqDTO.getNickName());

                    Optional<GroupApplyLog> groupApplyLog = groupApplyLogRegistory.findByMemberIdAndGroupId(member.getId(), groupId);

                    if (groupApplyLog.isPresent()) {

                        if (groupApplyLog.get().getStatus() == 1) {
                            throw new CustomException(Code.C509);
                        } else {
                            // TODO : 1.거절된 경우 Log.status를 어떻게 처리할 것인지?
                            groupApplyLogRegistory.delete(groupApplyLog.get());
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

    // TODO : 방장이 나갈경우는 어떻게 하는가?
    @Transactional
    public ApiResponse leaveGroup(Long groupId) {
        ApiResponse result = new ApiResponse();

        Optional<Group> checkGroup = groupRepository.findById(groupId);

        if (checkGroup.isPresent()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication.getPrincipal().equals("anonymousUser")) {
                throw new CustomException(Code.C501);
            }

            Long currUserId = Long.parseLong(((UserDetails) authentication.getPrincipal()).getUsername());

            Optional<Member> checkCurrUser = memberRepository.findById(currUserId);

            if (checkCurrUser.isPresent()) {
                Member currUser = checkCurrUser.get();

                Optional<GroupApplyLog> groupApplyLog = groupApplyLogRegistory.findByMemberIdAndGroupId(currUserId, groupId);

                if (groupApplyLog.isPresent()) {
                    if (groupApplyLog.get().getStatus() == 1) {
                        // TODO : 2.탈톼한 경우 Log.status를 어떻게 처리할 것인지?
                        groupApplyLogRegistory.delete(groupApplyLog.get());

                        Optional<MemberGroup> memberGroup = memberGroupRepository.findByMemberIdAndGroupId(currUserId, groupId);

                        memberGroupRepository.delete(memberGroup.get());

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

        Optional<Group> checkGroup = groupRepository.findById(groupId);

        if (checkGroup.isPresent()) {
            Group group = checkGroup.get();

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication.getPrincipal().equals("anonymous")) {
                throw new CustomException(Code.C501);
            }

            Long currUserId = Long.parseLong(((UserDetails) authentication.getPrincipal()).getUsername());

            Optional<Member> checkCurrUser = memberRepository.findById(currUserId);

            if (checkCurrUser.isPresent()) {
                Member currUser = checkCurrUser.get();

                if (currUser.getId() == group.getLeader().getId()) {
                    Member member = memberRepository.findByNickName(leaderTossReqDTO.getNickName());

                    // 현재 넘겨받는 사람이 리더인지 아닌지 봐야한다.
                    Optional<MemberGroup> leaderGroup = memberGroupRepository.findByGroupIdAndMemberId(groupId, currUserId);
                    Optional<MemberGroup> memberGroup = memberGroupRepository.findByGroupIdAndMemberId(groupId, member.getId());

                    if (memberGroup.isPresent() && leaderGroup.isPresent()) {
                        group.setLeader(member);

                        leaderGroup.get().setGroupRole(GroupRole.MEMBER);
                        memberGroup.get().setGroupRole(GroupRole.LEADER);

                        groupRepository.save(group);
                        memberGroupRepository.save(memberGroup.get());

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
        Optional<Group> group = groupRepository.findById(groupId);

        if (group != null) {
            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (!authentication.getAuthorities().toString().equals("[ROLE_ANONYMOUS]")) {

                    UserDetails currUser = (UserDetails) authentication.getPrincipal();

                    if (currUser.getUsername().equals("" + group.get().getLeader().getId())) {
                        Member member = memberRepository.findByNickName(groupKickReqDTO.getNickName());

                        Optional<GroupApplyLog> groupApplyLog = groupApplyLogRegistory.findByMemberIdAndGroupId(member.getId(), groupId);
                        Optional<MemberGroup> memberGroup = memberGroupRepository.findByMemberIdAndGroupId(member.getId(), groupId);

                        if (groupApplyLog.isPresent() && memberGroup.isPresent()) {
                            groupApplyLog.get().setStatus(2);
                            groupApplyLog.get().setUpdate_date(new Date());

                            memberGroupRepository.delete(memberGroup.get());

                            result.setCode(200);
                            result.setMessage("GROUP MEMBER KICK SUCCESS");
                        } else {
                            throw new CustomException(Code.C520);
                        }

                    } else {
                        throw new CustomException(Code.C565);
                    }
                }
            } catch (Exception e) {
                throw new CustomException(Code.C501);
            }
        } else {
            throw new CustomException(Code.C510);
        }

        return result;
    }

    public ApiResponse updateForm(Long groupId, HttpServletRequest request) {
        ApiResponse result = new ApiResponse();

        Optional<Group> checkGroup = groupRepository.findById(groupId);

        if (checkGroup.isPresent()) {
            Group group = checkGroup.get();

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (authentication.getPrincipal().equals("anonymousUser")) {
                throw new CustomException(Code.C501);
            }

            Long currUserId = Long.parseLong(((UserDetails) authentication.getPrincipal()).getUsername());

            Optional<Member> checkCurrUser = memberRepository.findById(currUserId);

            if (checkCurrUser.isPresent()) {
                Member currUser = checkCurrUser.get();

                if (currUser.getId() == group.getLeader().getId()) {

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
                } else {
                    throw new CustomException(Code.C565);
                }
            } else {
                throw new CustomException(Code.C503);
            }
        }

        return result;
    }
}
