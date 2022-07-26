package com.A108.Watchme.auth;

import com.A108.Watchme.Exception.AuthenticationException;
import com.A108.Watchme.Repository.MemberRepository;
import com.A108.Watchme.VO.ENUM.ErrorCode;
import com.A108.Watchme.VO.Entity.Member;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email);
        if(member==null){
            throw new AuthenticationException(ErrorCode.UsernameOrPasswordNotFoundException);
        }
        return new PrincipalDetails(member);
    }
}
