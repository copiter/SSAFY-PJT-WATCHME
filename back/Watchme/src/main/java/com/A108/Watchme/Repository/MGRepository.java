package com.A108.Watchme.Repository;


import com.A108.Watchme.VO.Entity.MemberGroup;
import com.A108.Watchme.VO.Entity.group.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MGRepository extends JpaRepository<MemberGroup, Long> {

    Page<MemberGroup> findByMemberId(Long id, Pageable mgRoom);
    Optional<MemberGroup> findByMemberIdAndGroupId(Long memberId, Long id);
}
