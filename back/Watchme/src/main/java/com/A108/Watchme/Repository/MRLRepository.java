package com.A108.Watchme.Repository;

import com.A108.Watchme.DTO.Room.RoomDetMemDTO;
import com.A108.Watchme.DTO.Sprint.SprintData;
import com.A108.Watchme.VO.ENUM.Status;
import com.A108.Watchme.VO.Entity.log.MemberRoomLog;
import com.A108.Watchme.VO.Entity.member.Member;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface MRLRepository extends JpaRepository<MemberRoomLog, Long> {

    List<MemberRoomLog> findAllByMemberId(Long memberId);
    List<MemberRoomLog> findByJoinedAtAfter(Timestamp date);
//    List<MemberRoomLog> findBymember_idBystart_atAfter(Long id, Date date);
    List<MemberRoomLog> findByRoomIdIn(List<Long> roomId);
    Optional<MemberRoomLog> findByMember_idAndRoom_id(Long memberId, Long roomId);

    List<MemberRoomLog> findByMemberIdAndRoomIdIn(Long memberId, List<Long> roomId);

    List<MemberRoomLog> findAllByRoomIdAndStatus(Long roomId, Status status);

    @Query(value = "SELECT SUM(mrl.studyTime) " +
            "FROM MemberRoomLog mrl " +
            "WHERE room_id= ?1")
    Optional<Integer> getSprintData(Long roomId);
    Optional<MemberRoomLog> findTopByRoomIdOrderByStudyTimeDesc(Long roomId);

    List<MemberRoomLog> findByMember_idAndStartAtAfter(Long memberId, Date date);
}
