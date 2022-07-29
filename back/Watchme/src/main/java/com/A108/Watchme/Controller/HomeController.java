package com.A108.Watchme.Controller;

import com.A108.Watchme.DTO.RoomCreateDTO;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Repository.*;
import com.A108.Watchme.VO.Entity.MemberRoomLog;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.VO.Entity.member.MemberInfo;
import com.A108.Watchme.VO.Entity.room.Room;
import com.A108.Watchme.VO.Entity.room.RoomInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RestController
public class HomeController {
    private HttpSession httpSession;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private MRLRepository mrlRepository;
    @Autowired
    private MemberInfoRepository memberInfoRepository;
    @Autowired
    private RoomInfoRepository roomInfoRepository;

    @GetMapping("home")
    public Map<String, String> home(HttpServletRequest request) {
        Map<String, String> map = new LinkedHashMap<>();
        httpSession=request.getSession(false);
        if(httpSession!=null){
            map.put("email", (String)httpSession.getAttribute("email"));
            map.put("image",(String)httpSession.getAttribute("image"));
            map.put("name",(String)httpSession.getAttribute("name"));
        }
        else{
            throw new RuntimeException("잘못된 접근");
        }
        return map;
    }

    @PostMapping("/addRoom")
    public ApiResponse addRoom(@RequestBody RoomCreateDTO roomCreateDTO){

//        System.out.println(roomCreateDTO.getRoomName());

        if(roomCreateDTO.getImageLink() == null){
            roomCreateDTO.setImageLink("https://popoimages.s3.ap-northeast-2.amazonaws.com/StudyRoom.jpg");
        }

        ApiResponse result = new ApiResponse();

        try{

            Room newRoom = roomRepository.save(Room.builder()
                    .roomName(roomCreateDTO.getRoomName())
                    .status(roomCreateDTO.getStatus())
                    .view(roomCreateDTO.getView())
                    .member(roomCreateDTO.getMember())
                    .sprint(roomCreateDTO.getSprint())
                            .build());

            roomInfoRepository.save(RoomInfo.builder()
                    .room(newRoom)
                    .roomPwd(roomCreateDTO.getRoomPwd())
                    .imageLink(roomCreateDTO.getImageLink())
                    .num(0)
                    .score(0)
                    .build());

            System.out.println(newRoom.getId());

            //
            Timestamp ttime = new Timestamp(System.currentTimeMillis());

            Calendar cal = Calendar.getInstance();
            cal.setTime(ttime);
            cal.add(Calendar.DATE, -3);

            ttime.setTime(cal.getTime().getTime());

            System.out.println(ttime);
            //
            MemberRoomLog newMRL = mrlRepository.save(MemberRoomLog.builder()
                    .room(newRoom)
                    .startAt(ttime)
                    .member(memberRepository.findById(1L).get())
                    .build());

            result.setCode(200);
        } catch(Exception e){
            e.printStackTrace();
            result.setCode(500);
        }

        return result;
    }

    @GetMapping("/main")
    public ApiResponse root(HttpServletRequest request){

        ApiResponse result = new ApiResponse();

        try{
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if(!authentication.getAuthorities().toString().equals("[ROLE_ANONYMOUS]")){
                Map<String, Object> userInfo = new LinkedHashMap<>();

                UserDetails currUser = (UserDetails)authentication.getPrincipal();

                Member member = memberRepository.findByEmail(currUser.getUsername());
                MemberInfo memberInfo = memberInfoRepository.findById(member.getId()).get();

                userInfo.put("username",member.getNickName());
                userInfo.put("groups",member.getGroups());
                userInfo.put("studyDay",memberInfo.getStudyTimeDay());
                userInfo.put("studyWeek",memberInfo.getStudyTimeWeek());
                userInfo.put("studyMonth",memberInfo.getStudyTimeMonth());

                result.setResponseData(userInfo);

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
