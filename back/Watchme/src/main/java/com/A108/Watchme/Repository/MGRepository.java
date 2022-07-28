package com.A108.Watchme.Repository;


import com.A108.Watchme.VO.Entity.MemberGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MGRepository extends JpaRepository<MemberGroup, Long> {

    public List<MemberGroup> findByMember_id(Long id);

}
