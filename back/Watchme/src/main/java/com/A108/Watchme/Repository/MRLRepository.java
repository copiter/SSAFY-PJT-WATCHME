package com.A108.Watchme.Repository;

import com.A108.Watchme.DTO.Room.RoomDetMemDTO;
import com.A108.Watchme.VO.ENUM.Status;
import com.A108.Watchme.VO.Entity.log.MemberRoomLog;
import com.A108.Watchme.VO.Entity.member.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public interface MRLRepository extends JpaRepository<MemberRoomLog, Long> {

    List<MemberRoomLog> findByMemberId(Long memberId);

//    @Query("SELECT nick_name as nickName, count(nick_name) as penalty\n" +
//            "FROM penalty_log inner join (SELECT member.member_id, nick_name\n" +
//            "                            FROM  member\n" +
//            "                            left join member_room_log mrl on member.member_id = mrl.member_id\n" +
//            "                            where room_id=:roomId and mrl.status='NO') as m2 on m2.member_id = penalty_log.member_id\n" +
//            "GROUP BY nick_name")
//    List<RoomDetMemDTO> getMembersPenalty(@Param("roomId") Long roomId);
    List<MemberRoomLog> findByRoomIdAndStatus(Long roomId, Status status);
//    List<MemberRoomLog> findByStartAtAfter(Timestamp date);
//    List<MemberRoomLog> findBymember_idBystart_atAfter(Long id, Date date);
    Optional<MemberRoomLog> findByMemberIdAndRoomId(Long memberId, Long roomId);
}
