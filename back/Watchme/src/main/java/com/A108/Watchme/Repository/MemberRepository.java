package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    public Member findByEmail(String email);
    public List<Member> findByEmailAndNickName(String email, String NickName);

    public Member findByNickName(String nickName);
}
