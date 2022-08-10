package com.A108.Watchme.Repository;


import com.A108.Watchme.VO.Entity.MemberGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MGRepository extends JpaRepository<MemberGroup, Long> {

    List<MemberGroup> findByMemberId(Long id);

    Optional<MemberGroup> findByMemberIdAndGroupId(Long memberId, Long id);
}
