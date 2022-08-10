package com.A108.Watchme.Http;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ApiResponse {
    private int code = HttpStatus.OK.value();
    private String message;
    private Map responseData = new HashMap();

    public ApiResponse() {}

    public ApiResponse(int code, String message, Map responseData) {
        this.setCode(code);
        this.setMessage(message);
        this.responseData = responseData;
    }

    public ApiResponse(Code code){
        this.setCode(code.getCode());
        this.setMessage(code.getMessage());
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public void setResponseData(String key, Object value) {
        this.responseData.put(key, value);
    }
}
