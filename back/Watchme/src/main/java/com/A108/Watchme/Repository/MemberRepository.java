package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface MemberRepository extends JpaRepository<Member, Long> {
    public Member findByEmail(String email);
    List<Member> findByEmailAndNickName(String email, String NickName);
}
