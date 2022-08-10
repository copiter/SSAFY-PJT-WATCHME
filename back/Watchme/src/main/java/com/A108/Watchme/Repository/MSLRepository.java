package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.log.MemberSprintLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MSLRepository extends JpaRepository<MemberSprintLog, Long> {
    Optional<MemberSprintLog> findByMemberIdAndSprintId(Long memberId, Long sprintId);

    List<MemberSprintLog> findAllBySprintId(Long sprintId);
}
