package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.log.PointLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PointLogRepository extends JpaRepository<PointLog, Long> {
    List<PointLog> findAllByMemberId(Long id);
    List<PointLog> findAllByMemberIdAndSprintId(Long memberId, Long sprintId);

    Optional<PointLog> findByMemberIdAndSprintIdAndFinish(Long memberId, Long sprintId, Integer finish);
}
