package com.A108.Watchme.Exception;

import com.A108.Watchme.Http.Code;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException{
    private final Code code;
}
