package com.A108.Watchme.Http;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class ApiResponse {
    private int code = HttpStatus.OK.value();
    private String message;

    public ApiResponse() {}

    public ApiResponse(int code, String message) {
        this.setCode(code);
        this.setMessage(message);
    }

    public void setResult(String message) {
        this.setMessage(message);
    }
}
