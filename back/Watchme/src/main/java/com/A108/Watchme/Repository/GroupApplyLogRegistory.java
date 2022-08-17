package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.MemberGroup;
import com.A108.Watchme.VO.Entity.log.GroupApplyLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupApplyLogRegistory extends JpaRepository<GroupApplyLog, Long> {

    List<GroupApplyLog> findAllByMemberId(Long id);
    List<GroupApplyLog> findAllByGroupId(Long id);
    long countByGroupIdAndStatus(Long id, int status);
    Optional<GroupApplyLog> findByMemberIdAndGroupId(Long id, Long groupId);
}
