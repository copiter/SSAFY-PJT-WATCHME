package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.MemberGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberGroupRepository extends JpaRepository<MemberGroup, Long> {
    Optional<MemberGroup> findByMemberIdAndGroupId(Long id, Long groupId);


    List<MemberGroup> findAllByGroupId(Long id);

    Optional<MemberGroup> findByGroupIdAndMemberId(Long groupId, Long MemberId);
}
