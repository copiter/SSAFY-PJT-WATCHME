package com.A108.Watchme.Service;

import com.A108.Watchme.DTO.*;
import com.A108.Watchme.DTO.group.GetGroupsDTO;
import com.A108.Watchme.DTO.group.GroupApplyDTO;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Repository.*;
import com.A108.Watchme.VO.ENUM.CategoryList;
import com.A108.Watchme.VO.Entity.Category;
import com.A108.Watchme.VO.Entity.MemberGroup;
import com.A108.Watchme.VO.Entity.group.Group;
import com.A108.Watchme.VO.Entity.group.GroupCategory;
import com.A108.Watchme.VO.Entity.log.GroupApplyLog;
import com.A108.Watchme.VO.Entity.log.PenaltyLog;
import com.A108.Watchme.VO.Entity.member.Member;
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
    private final GroupApplyLogRegistory groupApplyLogRegistory;
    private final PenaltyLogRegistory penaltyLogRegistory;
    private final MemberRepository memberRepository;
    private final MemberGroupRepository memberGroupRepository;


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
        ApiResponse apiResponse = new ApiResponse();
        return apiResponse;
    }

    public ApiResponse updateGroup(Long groupId, GroupUpdateReqDTO groupUpdateReqDTO, MultipartFile image, HttpServletRequest request) {
        ApiResponse apiResponse = new ApiResponse();
        return apiResponse;
    }

    public ApiResponse deleteGroup(Long groupId) {
        ApiResponse apiResponse = new ApiResponse();
        return apiResponse;
    }

    public ApiResponse getApplyList(Long groupId) {
        ApiResponse result = new ApiResponse();
        Optional<Group> group = groupRepository.findById(groupId);

        if (group != null) {
            try {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (!authentication.getAuthorities().toString().equals("[ROLE_ANONYMOUS]")) {

                    UserDetails currUser = (UserDetails) authentication.getPrincipal();
                    if (currUser.getUsername().equals(group.get().getLeader())) {
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
                        result.setResponseData("applieds", getApplys);
                        result.setCode(200);
                        result.setMessage("GROUP APPLY LIST SUCESS");
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
                        .apply_date( new Date())
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

                if (currUser.getUsername().equals(group.get().getLeader())) {

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


                    if (currUser.getUsername().equals(group.get().getLeader())) {

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


                    if(groupApplyLog != null){
                        groupApplyLog.get().setStatus(5);
                        groupApplyLog.get().setUpdate_date(new Date());

                        Optional<MemberGroup> memberGroup =  memberGroupRepository.findByMemberIdAndGroupId(member.getId(), groupId);
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

                    if (currUser.getUsername().equals(group.get().getLeader())) {

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

                    if (currUser.getUsername().equals(group.get().getLeader())) {
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
}
