package com.A108.Watchme.utils;

import javax.servlet.http.HttpServletRequest;

public class HeaderUtil {

    private final static String HEADER_AUTHORIZATION = "accessToken";

    public static String getAccessToken(HttpServletRequest request) {
        String headerValue = request.getHeader(HEADER_AUTHORIZATION);
        System.out.println("Header Value= " + headerValue);

        if (headerValue == null) {
            return null;
        }

        return headerValue;
    }
}
