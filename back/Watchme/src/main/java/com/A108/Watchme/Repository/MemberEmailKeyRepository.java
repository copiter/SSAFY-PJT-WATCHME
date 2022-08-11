package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.member.MemberEmailKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberEmailKeyRepository extends JpaRepository<MemberEmailKey, Long> {

    MemberEmailKey findByEmailKey(String emailKey);
}
