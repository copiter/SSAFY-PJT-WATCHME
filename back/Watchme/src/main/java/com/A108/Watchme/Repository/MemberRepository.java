package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;


public interface MemberRepository extends JpaRepository<Member, Long> {
    public Member findByEmail(String email);
}
