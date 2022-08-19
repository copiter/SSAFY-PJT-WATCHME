package com.A108.Watchme.Repository;

import com.A108.Watchme.VO.Entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);
    List<Member> findByEmailAndNickName(String email, String NickName);

    Optional<Member> findByNickName(String nickName);

}
