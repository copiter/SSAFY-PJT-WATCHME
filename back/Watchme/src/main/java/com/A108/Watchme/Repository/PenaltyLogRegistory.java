package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.log.PenaltyLog;
import com.A108.Watchme.VO.Entity.sprint.Sprint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PenaltyLogRegistory extends JpaRepository<PenaltyLog, Long> {
    List<PenaltyLog> findAllByMemberId(Long id);

    List<PenaltyLog> findAllByMemberIdAndSr_id(Long id, List<Sprint> sprintList);

}
