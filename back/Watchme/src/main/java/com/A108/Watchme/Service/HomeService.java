package com.A108.Watchme.Service;

import com.A108.Watchme.DTO.group.GroupDataDTO;
import com.A108.Watchme.DTO.MemberDataDTO;
import com.A108.Watchme.DTO.room.RoomDataDTO;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Repository.*;
import com.A108.Watchme.VO.Entity.MemberGroup;
import com.A108.Watchme.VO.Entity.group.Group;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.VO.Entity.room.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
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
        System.out.println("-----------------------------------");

            if(!authentication.getAuthorities().toString().equals("[ROLE_ANONYMOUS]")){

                UserDetails currUser = (UserDetails)authentication.getPrincipal();

                Member member = memberRepository.findById(Long.parseLong(currUser.getUsername())).get();

                MemberDataDTO resMember = MemberDataDTO.builder()
                        .email(member.getEmail())
                        .name(member.getMemberInfo().getName())
                        .nickName(member.getNickName())
                        .gender(member.getMemberInfo().getGender())
                        .birth(member.getMemberInfo().getBirth())
                        .profileImage(member.getMemberInfo().getImageLink())
                        .studyTimeToday(member.getMemberInfo().getStudyTimeDay())
                        .studyTimeWeek(member.getMemberInfo().getStudyTimeWeek())
                        .studyTimeMonth(member.getMemberInfo().getStudyTimeMonth())
                        .studyTimeTotal(member.getMemberInfo().getStudyTime())
                        .build();


                List<GroupDataDTO> resMyGroups = new LinkedList<>();
                List<MemberGroup> memberGroupList = mgRepository.findByMember_id(member.getId());
                for (MemberGroup mg :
                        memberGroupList) {
                    resMyGroups.add(GroupDataDTO.builder()
                            .ID(mg.getGroup().getId())
                            .groupName(mg.getGroup().getGroupName())
                            .groupImage(mg.getGroup().getGroupInfo().getImageLink())
                            .groupDescription(mg.getGroup().getGroupInfo().getDescription())
                            .groupMemberNo(mg.getGroup().getGroupInfo().getCurrMember())
                            .groupMemberMaxNo(mg.getGroup().getGroupInfo().getMaxMember())
                            .groupLookUp(mg.getGroup().getView())
                            .groupCategory(mg.getGroup().getCategory().stream().map(x->x.getCategory().getName()).collect(Collectors.toList()))
                            .build());
                }


                result.setResponseData("member", resMember);
                result.setResponseData("myGroups", resMyGroups);

            }


            PageRequest pageRequest = PageRequest.of(0,5);

            List<RoomDataDTO> resRoom = new LinkedList<>();


            List<Room> roomList = roomRepository.findAllByOrderByViewDesc(pageRequest).stream().collect(Collectors.toList());
            for (Room room :
                    roomList) {
                System.out.println("roomList = " + room.toString());
            }
            for (Room room :
                    roomList) {
                resRoom.add(RoomDataDTO.builder()
                        .ID(room.getId())
                        .URL(room.getId().toString())
                        .roomName(room.getRoomName())
                        .roomImage(room.getRoomInfo().getImageLink())
                        .roomDescription(room.getRoomInfo().getDescription())
                        .roomMemberNo(room.getRoomInfo().getCurrMember())
                        .roomMemberMaxNo(room.getRoomInfo().getMaxMember())
                        .roomLookUp(room.getView())
                        .roomCategory(room.getRoomCtg().getName())
                        .build());
            }

            result.setResponseData("rooms",resRoom);


            List<GroupDataDTO> resGroup = new LinkedList<>();
            List<Group> groupList = groupRepository.findAllByOrderByViewDesc(pageRequest).stream().collect(Collectors.toList());
            for (Group group:
                 groupList) {
                resGroup.add(GroupDataDTO.builder()
                        .ID(group.getId())
                        .groupName(group.getGroupName())
                        .groupImage(group.getGroupInfo().getImageLink())
                        .groupDescription(group.getGroupInfo().getDescription())
                        .groupMemberNo(group.getGroupInfo().getCurrMember())
                        .groupMemberMaxNo(group.getGroupInfo().getMaxMember())
                        .groupLookUp(group.getView())
                        .groupCategory(group.getCategory().stream().map(x->x.getCategory().getName()).collect(Collectors.toList()))
                        .build());
            }

            result.setResponseData("groups",resGroup);

            result.setMessage("homeview success");
            result.setCode(200);

        return result;
    }


}
