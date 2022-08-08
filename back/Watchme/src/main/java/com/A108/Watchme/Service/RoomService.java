package com.A108.Watchme.Service;

import com.A108.Watchme.DTO.GetRoomResDTO;
import com.A108.Watchme.DTO.PostRoomReqDTO;
import com.A108.Watchme.DTO.Room.JoinRoomDTO;
import com.A108.Watchme.DTO.Room.RoomUpdateDTO;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Repository.*;
import com.A108.Watchme.VO.ENUM.CategoryList;
import com.A108.Watchme.VO.ENUM.RoomStatus;
import com.A108.Watchme.VO.ENUM.Status;
import com.A108.Watchme.VO.Entity.Category;
import com.A108.Watchme.VO.Entity.log.MemberRoomLog;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.VO.Entity.room.Room;
import com.A108.Watchme.VO.Entity.room.RoomInfo;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.A;
import org.joda.time.DateTime;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM월 dd일 HH시 mm분");
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final MRLRepository mrlRepository;
    private final RoomInfoRepository roomInfoRepository;
    private final CategoryRepository categoryRepository;
    private final S3Uploader s3Uploader;

    public ApiResponse createRoom(PostRoomReqDTO postRoomReqDTO, MultipartFile images, HttpServletRequest request) {

        ApiResponse result = new ApiResponse();
        System.out.println(postRoomReqDTO.getCategoryName());

        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (!authentication.getAuthorities().toString().equals("[ROLE_ANONYMOUS]")) {
                System.out.println("heelo");
                String url = "https://popoimages.s3.ap-northeast-2.amazonaws.com/StudyRoom.jpg";

                try {
                    url = s3Uploader.upload(images, "Watchme");
                } catch (Exception e) {
                    e.printStackTrace();
                }


                UserDetails currUser = (UserDetails) authentication.getPrincipal();
                Member member = memberRepository.findByEmail(currUser.getUsername());
                CategoryList name = CategoryList.valueOf(postRoomReqDTO.getCategoryName());
                Category category = categoryRepository.findByName(name);

                Room room = Room.builder()
                        .roomName(postRoomReqDTO.getRoomName())
                        .member(member)
                        .roomCtg(category)
                        .status(RoomStatus.valueOf(postRoomReqDTO.getStatus()))
                        .view(0)
                        .build();

                RoomInfo roominfo = RoomInfo.builder()
                        .room(room)
                        .maxMember(postRoomReqDTO.getNum())
                        .pwd(postRoomReqDTO.getRoomPwd())
                        .currMember(0)
                        .endAt(format.parse(postRoomReqDTO.getEndTime()))
                        .description(postRoomReqDTO.getDescription())
                        .imageLink(url)
                        .display(postRoomReqDTO.getDisplay())
                        .build();
                System.out.println("TEST1");

                roomRepository.save(room);
                roomInfoRepository.save(roominfo);
                joinRoomFunc(room.getId());
                System.out.println("TEST2");
                result.setCode(200);
                result.setMessage("SUCCESS ADD&JOIN ROOM");
                result.setResponseData("roomId", room.getId());

            }

        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
        }

        return result;
    }


    public ApiResponse getRoomList(String ctgName, int page, String keyword) {
        ApiResponse result = new ApiResponse();

        System.out.println("roomService getRoomList : keyword = " + keyword);

        PageRequest pageRequest = PageRequest.of(page - 1, 10);
        List<Room> roomList;

        if (ctgName != null) {

            CategoryList name = CategoryList.valueOf(ctgName);

            Category category = categoryRepository.findByName(name);

            if (keyword == null) {
                System.out.println("ctg");
                roomList = roomRepository.findAllByRoomCtg(category, pageRequest).stream().collect(Collectors.toList());
            } else {
                System.out.println("ctg&search");
                roomList = roomRepository.findAllByRoomCtgAndRoomNameContaining(category, keyword, pageRequest).stream().collect(Collectors.toList());
            }

        } else {
            if (keyword == null) {
                System.out.println("nullAndnull");
                roomList = roomRepository.findAllByOrderByViewDesc(pageRequest).stream().collect(Collectors.toList());
            } else {
                System.out.println("search");
                roomList = roomRepository.findAllByRoomNameContaining(keyword, pageRequest).stream().collect(Collectors.toList());
            }

        }

        List<GetRoomResDTO> getRooms = new LinkedList<>();
        for (Room room : roomList) {
            getRooms.add(new GetRoomResDTO().builder()
                    .id(room.getId())
                    .roomImage(room.getRoomInfo().getImageLink())
                    .roomName(room.getRoomName())
                    .roomStatus(room.getStatus())
                    .ctgName(room.getRoomCtg().getName())
                    .description(room.getRoomInfo().getDescription())
                    .endTime(simpleDateFormat.format(room.getRoomInfo().getEndAt()))
                    .secret(room.getRoomInfo().getPwd() != null)
                    .nowNum(room.getRoomInfo().getCurrMember())
                    .maxNum(room.getRoomInfo().getMaxMember())
                    .build()
            );
        }
        result.setResponseData("rooms", getRooms);
        result.setMessage("GETROOMS SUCCESS");
        result.setCode(200);
        return result;
    }


    public ApiResponse joinRoom(Long roomId, JoinRoomDTO joinRoomDTO) {
        ApiResponse result = new ApiResponse();
        try {
            Integer roomPwd = roomRepository.findById(roomId).get().getRoomInfo().getPwd();
            int pwd;
            if(joinRoomDTO==null){
                pwd = -1;
            }
            else{
                pwd = joinRoomDTO.getPwd();
            }
            if(roomPwd==null || roomPwd== pwd){
                if(roomPeople(roomId, 1)){
                    joinRoomFunc(roomId);
                    result.setMessage("JOIN SUCCESS");
                    result.setCode(200);
                }
                else{
                    result.setMessage("TOO MANY PEOPLE");
                    result.setCode(511);
                }
            }
            else{
                result.setMessage("PASSWORD FAIL");
                result.setCode(512);
            }


        } catch (Exception e) {
            e.printStackTrace();
            result.setCode(500);
        }

        return result;
    }

    public void joinRoomFunc(Long roomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().toString().equals("[ROLE_ANONYMOUS]")) {
            UserDetails currUser = (UserDetails) authentication.getPrincipal();
            Member member = memberRepository.findByEmail(currUser.getUsername());

            mrlRepository.save(new MemberRoomLog().builder()
                    .room(roomRepository.findById(roomId).get())
                    .member(member)
                    .status(Status.NO) // 시간이 정산되었는지 여부
                    .joinedAt(DateTime.now().toDate())
                    .build()
            );
        }
    }

    public void outRoomFunc(Long roomId, Long memberId) {
        MemberRoomLog memberRoomLog = mrlRepository.findByMemberIdAndRoomId(memberId, roomId);
        // Status가 NO인 경우에만
        roomPeople(roomId, -1);
        if (memberRoomLog.getStatus().equals(Status.NO)) {
            Long diff = DateTime.now().toDate().getTime() - memberRoomLog.getJoinedAt().getTime();
            int addTime = (int) (diff / (60 * 1000) % 60);
            memberRoomLog.setStudyTime(memberRoomLog.getStudyTime() + addTime);
        }
    }

    public ApiResponse kickMember(Long roomId, Long memberId) {
        // Member가 방장인지 조회
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id = Long.parseLong(((UserDetails) authentication.getPrincipal()).getUsername());
        Long owner_id = roomRepository.findById(roomId).get().getMember().getId();

        // 방장인 경우
        if (id == owner_id) {
            outRoomFunc(memberId, roomId);
        }
        return null;
        //
    }

    // 방떠날시 룸로그 정보 저장해줘야함.
    public ApiResponse endRoom(Long roomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(((UserDetails) authentication.getPrincipal()).getUsername());


        return null;
    }

    public ApiResponse updateRoom(Long roomId, RoomUpdateDTO roomUpdateDTO, MultipartFile images) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(((UserDetails) authentication.getPrincipal()).getUsername());
        CategoryList name = CategoryList.valueOf(roomUpdateDTO.getRoomCategory());
        Category category = categoryRepository.findByName(name);
        Room room = roomRepository.findById(roomId).get();
        String url = room.getRoomInfo().getImageLink();
        ApiResponse apiResponse = new ApiResponse();
        if (images!=null) {
            try {
                url = s3Uploader.upload(images, "Watchme");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (memberId == room.getMember().getId()) {
            try {
                System.out.println("update");
                room.setRoomCtg(category);
                room.setRoomName(roomUpdateDTO.getRoomName());
                room.setStatus(RoomStatus.valueOf(roomUpdateDTO.getStatus()));
                RoomInfo roomInfo = room.getRoomInfo();
                roomInfo = RoomInfo.builder()
                        .description(roomUpdateDTO.getRoomDescription())
                        .room(room)
                        .imageLink(url)
                        .endAt(format.parse(roomUpdateDTO.getEndAt()))
                        .maxMember(roomUpdateDTO.getRoomMemberMaxNo())
                        .pwd(roomUpdateDTO.getPwd())
                        .build();
                roomInfoRepository.save(roomInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }

            apiResponse.setCode(200);
            apiResponse.setMessage("UPDATE SUCCESS");

        }
        return apiResponse;
    }
    public ApiResponse outRoom(Long roomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(((UserDetails) authentication.getPrincipal()).getUsername());

        outRoomFunc(memberId, roomId);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(200);
        apiResponse.setMessage("OUT SUCCESS");


        return apiResponse;
    }

    public boolean roomPeople(Long roomId, int num) {
        Room room = roomRepository.findById(roomId).get();
        if (room.getRoomInfo().getMaxMember() > (room.getRoomInfo().getCurrMember() + num)) {
            room.getRoomInfo().setCurrMember(room.getRoomInfo().getMaxMember() + num);
            return true;
        }
        return false;

    }
}
