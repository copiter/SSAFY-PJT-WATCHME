package com.A108.Watchme.Controller;

import com.A108.Watchme.DTO.RoomJoinDTO;
import com.A108.Watchme.DTO.RoomLeaveDTO;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Repository.*;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.VO.Entity.room.Room;
import com.A108.Watchme.jwt.JwtProvider;
import io.openvidu.java.client.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/room")
public class RoomController {

    private OpenVidu openVidu;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private RoomRepository roomRepository;
    @Autowired
    private RoomInfoRepository roomInfoRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private MRLRepository mrlRepository;
    @Autowired
    private MemberInfoRepository memberInfoRepository;

    private JwtProvider jwtProvider;

    // 현재 돌아가고 있는 세션
    // Map <룸아이디, 세션>
    private Map<Long, Session> mapSessions = new ConcurrentHashMap<>();

    // 토큰(OpenVidu에서 만들어준) 세션과
    //
    private Map<Long, Map<String, OpenViduRole>> mapSessionNamesTokens = new ConcurrentHashMap<>();

    // URL where our OpenVidu server is listening
    private String OPENVIDU_URL;
    // Secret shared with our OpenVidu server
    private String SECRET;


    public RoomController(@Value("${openvidu.secret}") String secret, @Value("${openvidu.url}") String openviduUrl) {
        this.SECRET = secret;
        this.OPENVIDU_URL = openviduUrl;
        this.openVidu = new OpenVidu(OPENVIDU_URL, SECRET);
    }


    @PostMapping("/joinRoom")
    public ApiResponse joinRoom(@RequestBody @Validated RoomJoinDTO roomJoinDTO) {
       ApiResponse result = new ApiResponse();

       try {
           Member member = memberRepository.findById(roomJoinDTO.getMemberId()).get();
           Room room = roomRepository.findById(roomJoinDTO.getRoomId()).get();

           System.out.println(member.getNickName() + "가 " + room.getRoomName() + "에 입장하려고 합니다.");

           String serverData = "{\"serverData\": \"" + member.getNickName() + "\"}";

           // openvidu 연결 설정
           ConnectionProperties connectionProperties = new ConnectionProperties.Builder()
                   .type(ConnectionType.WEBRTC)
                   .role(OpenViduRole.PUBLISHER)
                   .data(serverData)
                   .build();

           // 룸 아이디로 mapSession 탐색색
          if(this.mapSessions.get(room.getId()) != null){
              System.out.println("Existing session " + room.getRoomName());

              // 연결 설정으로 토큰을 발급
              String token = this.mapSessions.get(room.getId()).createConnection(connectionProperties).getToken();

              this.mapSessionNamesTokens.get(room.getId()).put(token, OpenViduRole.PUBLISHER);

              // 이걸 반환해줘야 한다.

              result.setMessage("JOIN ROOM SUCCESS");
              result.setResponseData("roomToken", token);
           }

          else {
              // 새로운 세션 열기
              System.out.println("New session " + room.getRoomName());

              // Create a new OpenVidu Session
              // 새 세션 열기
              Session session = this.openVidu.createSession();

              // 연결 설정으로 새로 토큰 만들어주기
              String token = session.createConnection(connectionProperties).getToken();

              // 룸과 세션을 연결
              this.mapSessions.put(room.getId(), session);
              this.mapSessionNamesTokens.put(room.getId(), new ConcurrentHashMap<>());
              this.mapSessionNamesTokens.get(room.getId()).put(token, OpenViduRole.PUBLISHER);

              result.setMessage("JOIN ROOM SUCCESS");
              result.setResponseData("roomToken", token);

              result.setCode(200);
          }

           result.setResponseData("roomName", room.getRoomName());
           result.setResponseData("roomLeader", room.getMember().getNickName());
           result.setResponseData("roomView", room.getView());
           result.setResponseData("roomStatus", room.getStatus());
           result.setResponseData("roomPeopleNum", this.mapSessionNamesTokens.get(room.getId()).size());


       }catch (Exception e){
           e.printStackTrace();
           result.setMessage("JOIN ROOM ERROR");
           result.setCode(500);
       }

       return result;
    }


    @PostMapping("/leave-room")
    public ApiResponse leaveRoom(@RequestBody RoomLeaveDTO roomLeaveDTO) {
        ApiResponse result = new ApiResponse();

        Member member = memberRepository.findById(roomLeaveDTO.getMemberId()).get();
        Room room = roomRepository.findById(roomLeaveDTO.getRoomId()).get();

        System.out.println(member.getNickName() +"님이 "+room.getRoomName()+"을 떠나십니다.");

        // 방이 존재할때
        if(this.mapSessions.get(room.getId()) != null && this.mapSessionNamesTokens.get(room.getId()) != null){

            // 만약 토큰이 존재한다면
            if (this.mapSessionNamesTokens.get(room.getId()).remove(room.getId()) != null) {
                // 만약 토큰이 있다면

                //내가 나갔는데 마지막 사람일 경우
                if(this.mapSessionNamesTokens.get(room.getId()).isEmpty()) {
                        //세션을 닫아버린다.
                        this.mapSessions.remove(room.getId());
                    }
                    result.setMessage("LEAVE SUCCESS");
                    result.setCode(200);
                } else {
                    result.setMessage("LEAVE ERROR - WRONG TOKEN");
                    result.setCode(500);
                }
            } else {
                result.setMessage("LEAVE ERROR - SESSION DOES NOT EXIST");
                result.setCode(500);
            }
        return result;
    }

//    @PostMapping("/change-rule")
//    public ApiResponse changeRule(@RequestBody RoomChangeDTO roomChangeDTO){
//
//    }

}
