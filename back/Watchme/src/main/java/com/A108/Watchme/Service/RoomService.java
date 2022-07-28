package com.A108.Watchme.Service;


import com.A108.Watchme.DTO.OpenViduDTO;
import com.A108.Watchme.DTO.RoomCreateDTO;
import com.A108.Watchme.DTO.RoomJoinDTO;
import com.A108.Watchme.DTO.RoomLeaveDTO;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Repository.MRLRepository;
import com.A108.Watchme.Repository.MemberRepository;
import com.A108.Watchme.Repository.RoomInfoRepository;
import com.A108.Watchme.Repository.RoomRepository;
import com.A108.Watchme.VO.Entity.MemberRoomLog;
import com.A108.Watchme.VO.Entity.room.Room;
import com.A108.Watchme.VO.Entity.room.RoomInfo;
import io.openvidu.java.client.*;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.Timestamp;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Service
public class RoomService {

    private final RoomRepository roomRepository;

    private final RoomInfoRepository roomInfoRepository;

    private final MRLRepository mrlRepository;

    private final MemberRepository memberRepository;

    //현재 돌아가고 있는 룸들
    private Map<Long, Session> mapSessions = new ConcurrentHashMap<>();

    //현재 룸들과 거기에 토큰들
    private Map<Long, Map<String, OpenViduRole>> mapSessionNamesTokens = new ConcurrentHashMap<>();


    public ApiResponse insertRoom(RoomCreateDTO roomCreateDTO) throws ParseException {
        ApiResponse result = new ApiResponse();

        Room room = roomRepository.save(Room.builder()
                        .roomName(roomCreateDTO.getRoomName())
                        .member(roomCreateDTO.getMember())
                        .status(roomCreateDTO.getStatus())
                        .sprint(roomCreateDTO.getSprint())
                        .view(0)
                .build());

        roomInfoRepository.save(RoomInfo.builder()
                        .roomPwd(roomCreateDTO.getRoomPwd())
                        .num(roomCreateDTO.getNum())
                        .score(0)
                        .diplsy(roomCreateDTO.getDisplay())
                .build());

        Timestamp ttime = new Timestamp(System.currentTimeMillis());

        Calendar cal = Calendar.getInstance();
        cal.setTime(ttime);
        cal.add(Calendar.DATE, -3);

        mrlRepository.save(MemberRoomLog.builder()
                        .room(room)
                        .startAt(ttime)
                        .member(roomCreateDTO.getMember())
                .build());

        result.setMessage("ROOM INSERT SUCCESS");
        result.setResponseData("DATA", "SUCESS");

        return result;
    }

    @PostMapping("/join-room")
    public ApiResponse joinRoom(RoomJoinDTO roomJoinDTO, OpenVidu openVidu){
        ApiResponse result = new ApiResponse();


        // 멤버의 OpenVidu 역할을 받아온다.
        // 우리는 전부 publisher이다.
        OpenViduRole role = OpenViduRole.PUBLISHER;

        // Optional data to be passed to other users when this user connects to the
        // video-call. In this case, a JSON with the value we stored in the HttpSession
        // object on login
        String serverData = "{\"serverData\": \"" + memberRepository.findById(roomJoinDTO.getMemberId()) + "\"}";

        // Build connectionProperties object with the serverData and the role
        ConnectionProperties connectionProperties= new ConnectionProperties.Builder().type(ConnectionType.WEBRTC)
                .role(role).data(serverData).build();

        // 들어갈 RoomID가 있는 방이 켜져 있는지 확인한다.
        // 만약 있다면 그냥 그 방에 들어간다.
        if(this.mapSessions.get(roomJoinDTO.getRoomId()) != null){
            try {
                
                // connectionProperties 토큰 만들어주기
                String token = this.mapSessions.get(roomJoinDTO.getRoomId()).createConnection(connectionProperties).getToken();
                
                // 토큰 보관소 업데이트
                this.mapSessionNamesTokens.get(roomJoinDTO.getRoomId()).put(token, role);

//                // Add all the needed attributes to the template
//                model.addAttribute("sessionName", sessionName);
//                model.addAttribute("token", token);
//                model.addAttribute("nickName", clientData);
//                model.addAttribute("userName", httpSession.getAttribute("loggedUser"));
//
//                // Return session.html template
//                return "session";


                result.setMessage("ROOM JOIN SUCCESS");
                result.setResponseData("DATA", "SUCCES");
                return result;
            }catch (Exception e){
                result.setMessage("ROOM JOIN ERROR");
                result.setResponseData("DATA", "FAIL");
                return result;
            }
        } else {
            // 방이 없으니 새로 만들어 줘야한다.

            try {

                Session session = openVidu.createSession();

                String token = session.createConnection(connectionProperties).getToken();

                this.mapSessions.put(roomJoinDTO.getRoomId(), session);
                this.mapSessionNamesTokens.put(roomJoinDTO.getRoomId(), new ConcurrentHashMap<>());
                this.mapSessionNamesTokens.get(roomJoinDTO.getRoomId()).put(token, role);

                result.setMessage("ROOM JOIN SUCCESS");
                result.setResponseData("DATA", "SUCCES");
                return result;
            }catch (Exception e) {
                result.setMessage("EMPTY ROOM JOIN ERROR");
                result.setResponseData("DATA", "FAIL");
                return result;
            }
        }
    }


    // 방에서 나갈 때
    @PostMapping(value = "/leave-room")
    public ApiResponse leaveRoom(RoomLeaveDTO roomLeaveDTO) {
        ApiResponse result = new ApiResponse();
        return result;
    }

}
