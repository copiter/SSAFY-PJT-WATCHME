package com.A108.Watchme.Service;

import com.A108.Watchme.DTO.group.GetGroupsDTO;
import com.A108.Watchme.DTO.group.GroupDataDTO;
import com.A108.Watchme.DTO.MemberDataDTO;
import com.A108.Watchme.DTO.Room.RoomDataDTO;
import com.A108.Watchme.DTO.group.getGroupList.SprintDTO;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Repository.*;
import com.A108.Watchme.VO.ENUM.CategoryList;
import com.A108.Watchme.VO.ENUM.Status;
import com.A108.Watchme.VO.Entity.MemberGroup;
import com.A108.Watchme.VO.Entity.group.Group;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.VO.Entity.room.Room;
import com.A108.Watchme.VO.Entity.sprint.Sprint;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class HomeService {
    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;
    private final GroupRepository groupRepository;
    private final MRLRepository mrlRepository;
    private final MGRepository mgRepository;


    public ApiResponse main(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        ApiResponse result = new ApiResponse();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat format2 = new SimpleDateFormat("HH:mm");
        SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

        System.out.println("-----------------------------------");

        if (!authentication.getAuthorities().toString().equals("[ROLE_ANONYMOUS]")) {

            Long currUserId = Long.parseLong(((UserDetails) authentication.getPrincipal()).getUsername());

            Optional<Member> checkCurrUser = memberRepository.findById(currUserId);

            if (checkCurrUser.isPresent()) {
                Member currUser = checkCurrUser.get();


                MemberDataDTO resMember = MemberDataDTO.builder()
                        .nickName(currUser.getNickName())
                        .profileImage(currUser.getMemberInfo().getImageLink())
                        .studyTimeToday(currUser.getMemberInfo().getStudyTimeDay())
                        .studyTimeWeek(currUser.getMemberInfo().getStudyTimeWeek())
                        .studyTimeMonth(currUser.getMemberInfo().getStudyTimeMonth())
                        .studyTimeTotal(currUser.getMemberInfo().getStudyTime())
                        .build();

                result.setResponseData("member", resMember);


                PageRequest mgRoom = PageRequest.of(0, 2);

                List<GroupDataDTO> resMyGroups = new LinkedList<>();

                List<MemberGroup> memberGroupList = mgRepository.findByMemberId(currUserId, mgRoom).stream().collect(Collectors.toList());

                for (MemberGroup mg :
                        memberGroupList) {
                    resMyGroups.add(GroupDataDTO.builder()
                            .id(mg.getGroup().getId())
                            .name(mg.getGroup().getGroupName())
                            .description(mg.getGroup().getGroupInfo().getDescription())
                            .currMember(mg.getGroup().getGroupInfo().getCurrMember())
                            .maxMember(mg.getGroup().getGroupInfo().getMaxMember())
                            .imgLink(mg.getGroup().getGroupInfo().getImageLink())
                            .build());
                }

                result.setResponseData("myGroups", resMyGroups);
            }
        }


        PageRequest prRoom = PageRequest.of(0, 6);

        List<RoomDataDTO> resRoom = new LinkedList<>();


        List<Room> roomList = roomRepository.findAllByStatusOrderByViewDesc(prRoom, Status.YES).stream().filter(x->!x.getRoomCtg().getName().equals(CategoryList.스프린트)).collect(Collectors.toList());
        for (Room room :
                roomList) {
            System.out.println("roomList = " + room.toString());
        }
        for (Room room :
                roomList) {
            resRoom.add(RoomDataDTO.builder()
                    .id(room.getId())
                    .roomName(room.getRoomName())
                    .roomStatus(room.getStatus().toString())
                    .ctgName(room.getRoomCtg().getName().toString())
                    .maxNum(room.getRoomInfo().getMaxMember())
                    .nowNum(room.getRoomInfo().getCurrMember())
                    .endTime(format3.format(room.getRoomInfo().getEndAt()))
                    .description(room.getRoomInfo().getDescription())
                    .roomImage(room.getRoomInfo().getImageLink())
                    .secret(room.getRoomInfo().getDisplay() == 2 ? true : false)
                    .build());
        }

        result.setResponseData("rooms", resRoom);


        PageRequest prGroup = PageRequest.of(0, 4);

        List<GetGroupsDTO> resGroup = new LinkedList<>();
        List<Group> groupList = groupRepository.findAllByOrderByViewDesc(prGroup).stream().collect(Collectors.toList());
        for (Group group :
                groupList) {
            List<Sprint> sprint = group.getSprints().stream().filter(x -> x.getSprintInfo().getStartAt().after(new Date())).collect(Collectors.toList());

            Sprint currSprint;

            resGroup.add(GetGroupsDTO.builder()
                    .id(group.getId())
                    .name(group.getGroupName())
                    .description(group.getGroupInfo().getDescription())
                    .currMember(group.getGroupInfo().getCurrMember())
                    .maxMember(group.getGroupInfo().getMaxMember())
                    .ctg(group.getCategory().stream().map(x -> x.getCategory().getName().toString()).collect(Collectors.toList()))
                    .createAt(format.format(group.getCreatedAt()))
                    .imgLink(group.getGroupInfo().getImageLink())
                    .secret(group.getDisplay() == 2 ? true : false)
                    .view(group.getView())
                    .sprint(!sprint.isEmpty()?SprintDTO.builder()
                            .name((currSprint = sprint.get(0)).getName())
                            .description(currSprint.getSprintInfo().getDescription())
                            .startAt(format.format(currSprint.getSprintInfo().getStartAt()))
                            .endAt(format.format(currSprint.getSprintInfo().getEndAt()))
                            .status(currSprint.getStatus().toString())
                            .build() : null
                    )
                    .build());
        }

        result.setResponseData("groups", resGroup);

        result.setMessage("homeview success");
        result.setCode(200);

        return result;
    }


}
