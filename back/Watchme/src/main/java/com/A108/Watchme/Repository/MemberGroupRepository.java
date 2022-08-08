package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.MemberGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberGroupRepository extends JpaRepository<MemberGroup, Long> {
    Optional<MemberGroup> findByMemberIdAndGroupId(Long id, Long groupId);
}
