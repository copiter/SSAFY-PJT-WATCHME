package com.A108.Watchme.Service;

import com.A108.Watchme.DTO.Room.*;
import com.A108.Watchme.Exception.CustomException;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Http.Code;
import com.A108.Watchme.Repository.*;
import com.A108.Watchme.VO.ENUM.CategoryList;
import com.A108.Watchme.VO.ENUM.Mode;
import com.A108.Watchme.VO.ENUM.Status;
import com.A108.Watchme.VO.Entity.Category;
import com.A108.Watchme.VO.Entity.log.MemberRoomLog;
import com.A108.Watchme.VO.Entity.log.PenaltyLog;
import com.A108.Watchme.VO.Entity.member.Member;
import com.A108.Watchme.VO.Entity.room.Room;
import com.A108.Watchme.VO.Entity.room.RoomInfo;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RoomService {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일 HH시 종료");

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    SimpleDateFormat format3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final MRLRepository mrlRepository;

    private final PenaltyLogRegistory penaltyLogRegistory;
    private final RoomInfoRepository roomInfoRepository;
    private final CategoryRepository categoryRepository;
    private final MemberRoomLogRepositoy memberRoomLogRepositoy;
    private final S3Uploader s3Uploader;

    @Transactional(rollbackFor = {Exception.class})
    public ApiResponse createRoom(PostRoomReqDTO postRoomReqDTO, MultipartFile images, Long memberId) {

        ApiResponse result = new ApiResponse();
        System.out.println(postRoomReqDTO.getCategoryName());

                String url = "https://popoimages.s3.ap-northeast-2.amazonaws.com/WatchMe/study.jpg";
            if(images!=null){
                try {
                    url = s3Uploader.upload(images, "Watchme");
                } catch (Exception e) {
                    throw new CustomException(Code.C512);
                }
            }
                Member member = memberRepository.findById(memberId).get();
                Category category;
                try{
                    CategoryList name = CategoryList.valueOf(postRoomReqDTO.getCategoryName());
                    category = categoryRepository.findByName(name);
                } catch(Exception e){
                    throw new CustomException(Code.C511);
                }
                Date date;
                try{
                    LocalDateTime localparseTime = LocalDateTime.parse(postRoomReqDTO.getEndTime());
                    String needDate = localparseTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    date = new Date(format.parse(needDate).getTime());
                    if(date.before(DateTime.now().toDate())){
                        throw new CustomException(Code.C527);
                    }
                } catch (Exception e){
                    throw new CustomException(Code.C599);
                }

                if(postRoomReqDTO.getRoomPwd()!=null){
                    try{
                        int pwd = postRoomReqDTO.getRoomPwd();
                        if(Integer.toString(pwd).length()!=4){
                            throw new CustomException(Code.C516);
                        }
                    } catch(Exception e){
                        throw new CustomException(Code.C525);
                    }
                }

                Room room = Room.builder()
                        .roomName(postRoomReqDTO.getRoomName())
                        .member(member)
                        .roomCtg(category)
                        .mode(Mode.valueOf(postRoomReqDTO.getMode()))
                        .status(Status.YES)
                        .view(0)
                        .build();

                RoomInfo roominfo = RoomInfo.builder()
                        .room(room)
                        .maxMember(postRoomReqDTO.getNum())
                        .pwd(postRoomReqDTO.getRoomPwd())
                        .currMember(0)
                        .endAt(date)
                        .description(postRoomReqDTO.getDescription())
                        .imageLink(url)
                        .build();
                roomRepository.save(room);
                roomInfoRepository.save(roominfo);
                joinRoomFunc(room.getId(), member.getId());
                result.setCode(200);
                result.setMessage("CREATE SUCCESS");
                result.setResponseData("roomId", room.getId());

        return result;
    }


    public ApiResponse getRoomList(String ctgName, int page, String keyword) {
        System.out.println(ctgName);
        System.out.println(keyword);
        ApiResponse result = new ApiResponse();
        PageRequest pageRequest = PageRequest.of(page - 1, 10);
        List<Room> roomList;
        try {
            if (ctgName != null) {
                CategoryList name = CategoryList.valueOf(ctgName);
                Category category = categoryRepository.findByName(name);
                if (keyword == null) {
                    roomList = roomRepository.findAllByRoomCtgAndStatus(category, pageRequest, Status.YES).stream().collect(Collectors.toList());
                } else {
                    roomList = roomRepository.findAllByRoomCtgAndStatusAndRoomNameContaining(category, Status.YES, keyword, pageRequest).stream().collect(Collectors.toList());
                }

            } else {
                if (keyword == null) {
                    roomList = roomRepository.findAllByStatusOrderByViewDesc(pageRequest, Status.YES).stream().collect(Collectors.toList());
                } else {
                    roomList = roomRepository.findAllByStatusAndRoomNameContaining(Status.YES, keyword, pageRequest).stream().collect(Collectors.toList());
                }

            }
        } catch(Exception e){
            throw new CustomException(Code.C521);
        }

        if(roomList.isEmpty()){
            throw new CustomException(Code.C520);
        }

        roomList = roomList.stream().filter(x->!x.getRoomCtg().getName().equals(CategoryList.스프린트)).collect(Collectors.toList());

        List<GetRoomResDTO> getRooms = new LinkedList<>();
        for (Room room : roomList) {
            if(room.getRoomCtg().getName().equals(CategoryList.스프린트)){
                continue;
            }
            getRooms.add(new GetRoomResDTO().builder()
                    .id(room.getId())
                    .roomImage(room.getRoomInfo().getImageLink())
                    .roomName(room.getRoomName())
                    .roomStatus(room.getMode())
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

    @Transactional(rollbackFor = {Exception.class})
    public ApiResponse joinRoom(Long roomId, JoinRoomDTO joinRoomDTO, Long memberId) {
        ApiResponse result = new ApiResponse();

        try {
            Room room = roomRepository.findById(roomId).get();
            if(!room.getStatus().toString().equals("YES")){
                throw new CustomException(Code.C522);
            }
            Integer roomPwd = room.getRoomInfo().getPwd();
            int pwd;
            if(joinRoomDTO==null){
                pwd = -1;
            }
            else{
                pwd = joinRoomDTO.getPwd();
            }
            if(roomPwd==null || roomPwd== pwd){
                if(roomPeople(roomId, 1)){
                    joinRoomFunc(roomId, memberId);
                    room.setView(room.getView()+1);
                    result.setMessage("JOIN SUCCESS");
                    result.setCode(200);
                }
                else{
                    throw new CustomException(Code.C550);
                }
            }
            else{
                throw new CustomException(Code.C551);
            }


        } catch (Exception e) {
            throw new CustomException(Code.C500);
        }

        return result;
    }

    public void joinRoomFunc(Long roomId, Long memberId) {

            Member member = memberRepository.findById(memberId).get();
            Optional<MemberRoomLog> memberRoomLog = mrlRepository.findByMember_idAndRoom_id(memberId, roomId);
            if(memberRoomLog.isPresent()){
                memberRoomLog.get().setJoinedAt(DateTime.now().toDate());
                memberRoomLog.get().setStatus(Status.NO);
                mrlRepository.save(memberRoomLog.get());
            }
            else{
                mrlRepository.save(MemberRoomLog.builder()
                        .room(roomRepository.findById(roomId).get())
                        .member(member)
                        .studyTime(0)
                        .status(Status.NO) // 시간이 정산되었는지 여부
                        .joinedAt(DateTime.now().toDate())
                        .build()
                );
            }
    }

    @Transactional(rollbackFor = {Exception.class})
    public void outRoomFunc(Long roomId, Long memberId) {
        MemberRoomLog memberRoomLog;
       try{
            memberRoomLog = mrlRepository.findByMember_idAndRoom_id(memberId, roomId).get();
       } catch (Exception e){
           throw new CustomException(Code.C523);
       }
        // Status가 NO인 경우에만
        roomPeople(roomId, -1);
        if (memberRoomLog.getStatus().equals(Status.NO)) {
            Long diff = DateTime.now().toDate().getTime() - memberRoomLog.getJoinedAt().getTime();
            int addTime = (int) (diff / (60 * 1000) % 60);
            int time = 0;
            if(memberRoomLog.getStudyTime()!=null){
                time = memberRoomLog.getStudyTime();
            }
            memberRoomLog.setStudyTime(time + addTime);
            memberRoomLog.setStatus(Status.YES);
            mrlRepository.save(memberRoomLog);
        } else {
            throw new CustomException(Code.C532);
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
    @Transactional(rollbackFor = {Exception.class})
    public ApiResponse updateRoom(Long roomId, RoomUpdateDTO roomUpdateDTO, MultipartFile images, Long memberId) {
        Room room;
        CategoryList name = CategoryList.valueOf(roomUpdateDTO.getRoomCategory());
        Category category = categoryRepository.findByName(name);
        try{
            room = roomRepository.findById(roomId).get();
        } catch(Exception e){
            throw new CustomException(Code.C522);
        }
        RoomInfo roomInfo = room.getRoomInfo();
        String url = room.getRoomInfo().getImageLink();
        ApiResponse apiResponse = new ApiResponse();
        if (images!=null) {
            try {
                url = s3Uploader.upload(images, "Watchme");
            } catch (Exception e) {
                throw new CustomException(Code.C512);
            }
        }
        Date date;
        try{
            try{
                LocalDateTime localparseTime = LocalDateTime.parse(roomUpdateDTO.getEndAt());
                String needDate = localparseTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                date = new Date(format.parse(needDate).getTime());
            } catch(Exception e){
                date=(format2.parse(roomUpdateDTO.getEndAt()));
            }
            if(date.before(DateTime.now().toDate())){
                throw new CustomException(Code.C527);
            }
        } catch (Exception e){
            throw new CustomException(Code.C599);
        }

        try{
            MemberRoomLog myRoomLog = mrlRepository.findByMember_idAndRoom_id(memberId, roomId).get();
            if(!myRoomLog.getStatus().equals(Status.NO)){
                throw new CustomException(Code.C595);
            }
        } catch (Exception e){
            throw new CustomException(Code.C523);
        }

        if(roomUpdateDTO.getPwd()!=null){
            try{
                int pwd = roomUpdateDTO.getPwd();
                if(Integer.toString(pwd).length()!=4){
                    throw new CustomException(Code.C516);
                }
            } catch(Exception e){
                throw new CustomException(Code.C525);
            }
        }

        if (memberId == room.getMember().getId()) {

                room.setRoomCtg(category);
                room.setRoomName(roomUpdateDTO.getRoomName());
                room.setMode(Mode.valueOf(roomUpdateDTO.getMode()));
                roomInfo.setDescription(roomUpdateDTO.getRoomDescription());
                roomInfo.setImageLink(url);
                roomInfo.setEndAt(date);
                roomInfo.setMaxMember(roomUpdateDTO.getRoomMemberMaxNo());
                roomInfo.setPwd(roomUpdateDTO.getPwd());
                roomInfoRepository.save(roomInfo);

            apiResponse.setCode(200);
            apiResponse.setMessage("UPDATE SUCCESS");

        }
        else{
            throw new CustomException(Code.C530);
        }
        return apiResponse;
    }
    public ApiResponse outRoom(Long roomId, Long memberId) {

        Room room;
        try{
            room = roomRepository.findById(roomId).get();
        } catch (Exception e){
            throw new CustomException(Code.C522);
        }
        System.out.println("___________");
        System.out.println(memberId);
        System.out.println(room.getMember().getId());
        if(room.getMember().getId() == memberId ){

            List<MemberRoomLog> mrlList;
            try{
                mrlList = memberRoomLogRepositoy.findAllByRoomIdAndStatus(roomId, Status.NO);
            } catch (Exception e) {
                throw new CustomException(Code.C554);
            }
            System.out.println(mrlList.size());
            if(mrlList.size() != 1){
                Long tossId = mrlList.get(0).getMember().getId();
                if(tossId == memberId)
                    tossId = mrlList.get(1).getMember().getId();
                tossLeaderFunc(roomId, tossId);
            }
        }

        outRoomFunc(roomId, memberId);
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(200);
        apiResponse.setMessage("OUT SUCCESS");

        return apiResponse;
    }



    public boolean roomPeople(Long roomId, int num) {
        Room room;
        try{
            room = roomRepository.findById(roomId).get();
        } catch (Exception e){
            throw new CustomException(Code.C522);
        }
        if (room.getRoomInfo().getMaxMember() >= (room.getRoomInfo().getCurrMember() + num)) {
            int man = room.getRoomInfo().getCurrMember();
            room.getRoomInfo().setCurrMember(man + num);
            // 마지막사람이 나가면 닫아줌
            if(man+num==0){
                room.setStatus(Status.NO);
            }
            return true;
        }
        return false;

    }

    public ApiResponse getRoomDet(Long roomId, Long memberId) {
        ApiResponse apiResponse = new ApiResponse();
        Room room;
        try{
            room = roomRepository.findById(roomId).get();
        } catch (Exception e) {
            throw new CustomException(Code.C522);
        }
        MemberRoomLog memberRoomLog;
        try{
            System.out.println(memberId);
            System.out.println(roomId);
            memberRoomLog = mrlRepository.findByMember_idAndRoom_id(memberId, roomId).get();
            if(!memberRoomLog.getStatus().equals(Status.NO)){
                throw new CustomException(Code.C595);
            }
        } catch (Exception e){
            throw new CustomException(Code.C523);
        }

        RoomDetMyDTO roomDetMyDTO = new RoomDetMyDTO();
        roomDetMyDTO.setStartTime(format.format(memberRoomLog.getJoinedAt()));
        roomDetMyDTO.setName(room.getRoomName());
        roomDetMyDTO.setMode(room.getMode());
        roomDetMyDTO.setLeaderName(room.getMember().getNickName());
        roomDetMyDTO.setLeaderTrue((room.getMember().getId().equals(memberId))? 1 : 0);
        roomDetMyDTO.setPenalty(penaltyLogRegistory.countByMemberIdAndRoomId(memberId, roomId));
        apiResponse.setCode(200);
        apiResponse.setMessage("GET ROOM MYDET SUCCESS");
        apiResponse.setResponseData("room", roomDetMyDTO);

        return apiResponse;
    }

    public ApiResponse getRoomMem(Long roomId, Long memberId) {
        ApiResponse apiResponse = new ApiResponse();
        List<RoomDetMemDTO> memDTOList = new LinkedList<>();
        try{
            Room room = roomRepository.findById(roomId).get();
        } catch(Exception e){
            throw new CustomException(Code.C522);
        }
        try{
            MemberRoomLog myRoomLog = mrlRepository.findByMember_idAndRoom_id(memberId, roomId).get();
            if(!myRoomLog.getStatus().equals(Status.NO)){
                throw new CustomException(Code.C595);
            }
        } catch (Exception e){
            throw new CustomException(Code.C523);
        }

        // 공부시간이 정산되지 않았으면 status가 NO임 => 공부중인 상태
        List<MemberRoomLog> memberRoomLogs = mrlRepository.findByRoomIdAndStatus(roomId, Status.NO);
        for(MemberRoomLog memberRoomLog : memberRoomLogs){
            Member member = memberRoomLog.getMember();
            int penalty = penaltyLogRegistory.countByMemberIdAndRoomId(member.getId(), roomId);
            memDTOList.add(new RoomDetMemDTO (member.getMemberInfo().getImageLink(), member.getNickName(), penalty));
        }
        apiResponse.setCode(200);
        apiResponse.setMessage("GET SUCCESS ROMMMEM");
        apiResponse.setResponseData("logs", memDTOList);
        return apiResponse;
    }

    public ApiResponse getRoomSetting(Long roomId, Long memberId) {
        ApiResponse apiResponse = new ApiResponse();
        Room room;
        try{
            room = roomRepository.findById(roomId).get();
        } catch(Exception e){
            throw new CustomException(Code.C522);
        }
        try{
            MemberRoomLog myRoomLog = mrlRepository.findByMember_idAndRoom_id(memberId, roomId).get();
            if(!myRoomLog.getStatus().equals(Status.NO)){
                throw new CustomException(Code.C595);
            }
        } catch (Exception e){
            throw new CustomException(Code.C523);
        }

        Long owner_id = room.getMember().getId();
        if(owner_id != memberId){
            throw new CustomException(Code.C530);
        }

        RoomDetSettingDTO roomDetSettingDTO = (RoomDetSettingDTO.builder()
                .mode(room.getMode().toString())
                .roomName(room.getRoomName())
                .categoryName(room.getRoomCtg().getName().toString())
                .description(room.getRoomInfo().getDescription())
                .roomPwd((room.getRoomInfo().getPwd()==null)? null:room.getRoomInfo().getPwd())
                .img(room.getRoomInfo().getImageLink())
                .num(room.getRoomInfo().getMaxMember())
                .endTime(format3.format(room.getRoomInfo().getEndAt()))
                .build());

        apiResponse.setCode(200);
        apiResponse.setMessage("SUCCESS GET SETTING");
        apiResponse.setResponseData("room", roomDetSettingDTO);

        return apiResponse;

    }

    public ApiResponse tossLeader(Long id, TossLeaderDTO tossLeaderDTO, Long memberId) {
        ApiResponse apiResponse = new ApiResponse();

        Long tossId;
        try {
            tossId = memberRepository.findByNickName(tossLeaderDTO.getNickName()).getId();
        } catch (Exception e) {
            throw new CustomException(Code.C504);
        }

        Long roomId;
        try {
            roomId = roomRepository.findById(id).get().getId();
        } catch(Exception e) {
            throw new CustomException(Code.C522);
        }
        tossLeaderFunc(roomId, tossId);

        apiResponse.setCode(200);
        apiResponse.setMessage("SUCCESS TOSS LEADER");
        return apiResponse;

    }

    private void tossLeaderFunc(Long roomId, Long tossId) {
        Member member = memberRepository.findById(tossId).get();
        Room room = roomRepository.findById(roomId).get();

        room.setMember(member);
        roomRepository.save(room);
    }
}
