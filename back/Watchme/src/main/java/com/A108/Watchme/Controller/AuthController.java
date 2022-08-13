package com.A108.Watchme.Controller;

import com.A108.Watchme.Exception.CustomException;
import com.A108.Watchme.Http.Code;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

@NoArgsConstructor
public class AuthController {
    public Long memberAuth(){
        Long memberId;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        try{
            memberId = Long.parseLong(((UserDetails)authentication.getPrincipal()).getUsername());
        }
        catch(Exception e){
            throw new CustomException(Code.C501);
        }

        return memberId;
    }
}