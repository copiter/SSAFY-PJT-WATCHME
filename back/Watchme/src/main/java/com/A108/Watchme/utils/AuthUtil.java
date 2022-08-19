package com.A108.Watchme.utils;

import com.A108.Watchme.Exception.CustomException;
import com.A108.Watchme.Http.Code;
import com.A108.Watchme.Repository.MemberRepository;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;


@NoArgsConstructor
@Component
public class AuthUtil {
    @Autowired
    private MemberRepository memberRepository;
    public Long memberAuth(){

        Long memberId;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            memberId = Long.parseLong(((UserDetails)authentication.getPrincipal()).getUsername());
            if(!memberRepository.existsById(memberId)){
                throw new CustomException(Code.C503);
            }
        }
        catch(Exception e){
            throw new CustomException(Code.C501);
        }

        return memberId;
    }
}