package com.A108.Watchme.Http;

import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class ResponseMap extends ApiResponse{
    private Map responseData = new HashMap();

    public ResponseMap() {
        setResult(responseData);
    }

    public void setResponseData(String key, Object value) {
        this.responseData.put(key, value);
    }
}
