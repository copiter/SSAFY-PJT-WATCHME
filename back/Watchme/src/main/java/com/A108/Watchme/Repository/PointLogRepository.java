package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.ENUM.Status;
import com.A108.Watchme.VO.Entity.group.Group;
import com.A108.Watchme.VO.Entity.log.PointLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PointLogRepository extends JpaRepository<PointLog, Long> {
    List<PointLog> findAllByMemberId(Long id);
    List<PointLog> findAllByMemberIdAndSprintId(Long memberId, Long sprintId);

    Optional<PointLog> findByMemberIdAndSprintIdAndFinish(Long memberId, Long sprintId, Integer finish);

    Page<PointLog> findAllByMemberIdOrderByCreatedAt(Long id,Pageable pageable);
}
