package com.A108.Watchme.Exception;

import com.A108.Watchme.DTO.ErrorDTO;
import com.A108.Watchme.Http.Code;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@Setter
public class CustomException extends RuntimeException{
    private final Code code;

    public CustomException(Code code){
        this.code = code;
    }
}
