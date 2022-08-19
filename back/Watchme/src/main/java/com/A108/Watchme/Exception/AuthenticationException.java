package com.A108.Watchme.Exception;

import com.A108.Watchme.VO.ENUM.ErrorCode;
import lombok.Getter;

@Getter
public class AuthenticationException extends RuntimeException{
    private final ErrorCode errorCode;

    public AuthenticationException(ErrorCode errorCode){
        this.errorCode = errorCode;
    }

}
