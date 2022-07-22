package com.A108.Watchme.Http;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ResponseMap extends ApiResponse{
    private Map responseData = new HashMap();
    private String message;
    public ResponseMap() {
        setResult(this.getMessage());
    }

    public void setResponseData(String key, Object value) {
        this.responseData.put(key, value);
    }
}
