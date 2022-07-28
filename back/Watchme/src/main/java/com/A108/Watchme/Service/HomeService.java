package com.A108.Watchme.Service;

import com.A108.Watchme.DTO.MemberDataDTO;
import com.A108.Watchme.DTO.RoomCreateDTO;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Repository.*;
import com.A108.Watchme.VO.Entity.MemberGroup;
import com.A108.Watchme.VO.Entity.group.Group;
import com.A108.Watchme.VO.Entity.log.MemberRoomLog;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.VO.Entity.member.MemberInfo;
import com.A108.Watchme.VO.Entity.room.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

@RequiredArgsConstructor
@Service
public class HomeService {

    private final MemberRepository memberRepository;
    private final RoomRepository roomRepository;
    private final GroupRepository groupRepository;
    private final MRLRepository mrlRepository;
    private final MemberInfoRepository memberInfoRepository;
    private final MGRepository mgRepository;

    public ApiResponse addRoom(RoomCreateDTO roomCreateDTO) {

        ApiResponse result = new ApiResponse();

        try{

            Room room = Room.builder()
                    .roomName(roomCreateDTO.getRoomName()).status(roomCreateDTO.getStatus()).view(roomCreateDTO.getView())
                    .build();

            Room newRoom = roomRepository.save(room);

            //
            Timestamp ttime = new Timestamp(System.currentTimeMillis());

            Calendar cal = Calendar.getInstance();
            cal.setTime(ttime);
            cal.add(Calendar.DATE, -3);

            ttime.setTime(cal.getTime().getTime());

            System.out.println(ttime);

            //

            MemberRoomLog newMRL = MemberRoomLog.builder().room(newRoom).startAt(ttime).member(memberRepository.findById(1L).get()).build();

            mrlRepository.save(newMRL);

            result.setCode(200);
        } catch(Exception e){
            e.printStackTrace();
            result.setCode(500);
        }

        return result;
    }

    public ApiResponse mainPage() {

        ApiResponse result = new ApiResponse();

        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if(!authentication.getAuthorities().toString().equals("[ROLE_ANONYMOUS]")){

                UserDetails currUser = (UserDetails)authentication.getPrincipal();

                Member member = memberRepository.findByEmail(currUser.getUsername());
                MemberInfo memberInfo = memberInfoRepository.findById(member.getId()).get();

                List<MemberGroup> memberGroupList = mgRepository.findByMember_id(member.getId());
//                List<Group> myGroups = groupRepository.findAllGroupByMember_id(member.getId());

                System.out.println("memberGroupList");

//                List<Group> myGroups = groupRepository.findAllByIdIn();

                System.out.println("myGroups");

                result.setResponseData("user", MemberDataDTO.builder().email(member.getEmail()).username(memberInfo.getName()).nickname(member.getNickName()).sex(memberInfo.getGender()).birthday(memberInfo.getBirth()).profileImage(memberInfo.getImageLink()).studyTimeToday(memberInfo.getStudyTimeDay()).studyTimeWeek(memberInfo.getStudyTimeWeek()).studyTimeMonth(memberInfo.getStudyTimeMonth()).studyTimeTotal(memberInfo.getStudyTime()).build());

//                result.setResponseData("myGroups", myGroups);
//                result.setResponseData("myGroups", GroupDataDTO.builder().build());

            }

            PageRequest pageRequest = PageRequest.of(0,5);

            result.setResponseData("rooms",roomRepository.findAllByOrderByViewDesc(pageRequest));
            result.setResponseData("groups",groupRepository.findAllByOrderByViewDesc(pageRequest));

            result.setMessage("homeview success");
            result.setCode(200);

        } catch(Exception e){
            e.printStackTrace();
            result.setCode(400);
            result.setMessage("homeview fail");
        }

        return result;
    }

}
