package com.A108.Watchme.Service;

import com.A108.Watchme.DTO.*;
import com.A108.Watchme.DTO.group.*;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Repository.*;
import com.A108.Watchme.VO.ENUM.CategoryList;
import com.A108.Watchme.VO.ENUM.RoomStatus;
import com.A108.Watchme.VO.ENUM.Status;
import com.A108.Watchme.VO.Entity.Category;
import com.A108.Watchme.VO.Entity.MemberGroup;
import com.A108.Watchme.VO.Entity.group.Group;
import com.A108.Watchme.VO.Entity.group.GroupCategory;
import com.A108.Watchme.VO.Entity.group.GroupInfo;
import com.A108.Watchme.VO.Entity.log.GroupApplyLog;
import com.A108.Watchme.VO.Entity.log.PenaltyLog;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.VO.Entity.room.Room;
import com.A108.Watchme.VO.Entity.room.RoomInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.swing.text.html.Option;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
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

    private final S3Uploader s3Uploader;

    public ApiResponse getGroupList(String groupCtg, String keyword, int page, int active, HttpServletRequest request) {
        ApiResponse apiResponse = new ApiResponse();
        PageRequest pageRequest = PageRequest.of(-1, 10);

        return apiResponse;
    }

    public ApiResponse getGroup(Long groupId, String pwd) {
        ApiResponse apiResponse = new ApiResponse();
        return apiResponse;
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

        if(check.isPresent()){
            Group group = check.get();

            result.setResponseData("group",UpdateFormResDTO.builder()
                    .id(group.getId())
                    .name(group.getGroupName())
                    .description(group.getGroupInfo().getDescription())
                    .maxMember(group.getGroupInfo().getMaxMember())
                    .ctg(group.getCategory().stream().map(x->x.getCategory().getName().toString()).collect(Collectors.toList()))
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
