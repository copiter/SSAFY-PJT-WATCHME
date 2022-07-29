package com.A108.Watchme.Service;

import com.A108.Watchme.DTO.RoomCreateDTO;
import com.A108.Watchme.Http.ApiResponse;
import com.A108.Watchme.Repository.MRLRepository;
import com.A108.Watchme.Repository.MemberRepository;
import com.A108.Watchme.Repository.RoomRepository;
import com.A108.Watchme.VO.Entity.log.MemberRoomLog;
import com.A108.Watchme.VO.Entity.room.Room;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Calendar;

@Service
@RequiredArgsConstructor
public class RoomService {

    private final RoomRepository roomRepository;
    private final MemberRepository memberRepository;
    private final MRLRepository mrlRepository;


    public ApiResponse createRoom(RoomCreateDTO roomCreateDTO) {

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

}
