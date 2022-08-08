package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.log.MemberRoomLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface MRLRepository extends JpaRepository<MemberRoomLog, Long> {

    List<MemberRoomLog> findBymember_id(Long memberId);
//    List<MemberRoomLog> findByStartAtAfter(Timestamp date);
//    List<MemberRoomLog> findBymember_idBystart_atAfter(Long id, Date date);
    MemberRoomLog findByMemberIdAndRoomId(Long memberId, Long roomId);
}
