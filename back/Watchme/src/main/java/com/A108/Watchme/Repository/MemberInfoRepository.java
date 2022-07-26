package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.member.MemberInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberInfoRepository extends JpaRepository<MemberInfo, Long> {
}
