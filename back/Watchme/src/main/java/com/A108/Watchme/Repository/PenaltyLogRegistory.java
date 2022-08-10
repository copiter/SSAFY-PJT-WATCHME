package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.log.PenaltyLog;
import com.A108.Watchme.VO.Entity.sprint.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PenaltyLogRegistory extends JpaRepository<PenaltyLog, Long> {
    List<PenaltyLog> findAllByMemberId(Long id);

    List<PenaltyLog> findAllByMemberIdAndSprintIn(Long id, List<Sprint> sprints);

//    @Query(value = "select new Integer(count(p.penalty_log_id))" +
//            "from Penalty_log p where p.member_id=:memberId and p.room_id=:roomId")
    Integer countByMemberIdAndRoomId(Long memberId, Long roomId);

    Integer countByRoomId(Long roomId);

}
