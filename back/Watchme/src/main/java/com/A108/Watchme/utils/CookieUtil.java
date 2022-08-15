package com.A108.Watchme.utils;

import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.util.SerializationUtils;
import org.springframework.http.ResponseCookie;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;

public class CookieUtil {
    static int count = 0;
    public static Optional<Cookie> getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    return Optional.of(cookie);
                }
            }
        }
        return Optional.empty();
    }

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
//        Cookie cookie = new Cookie(name, value);
//        cookie.setPath("/");
//        cookie.setHttpOnly(true);
//
////        cookie.setValue(LocalDateTime.now().toString());
//        cookie.setMaxAge(maxAge);
        ResponseCookie cookie1;
        if(name.equals("accessToken")){
            cookie1 =ResponseCookie.from(name,value)
                    .domain("")
                    .path("/")
                    .maxAge(maxAge)
                    .build();
        }
        else{
            cookie1 =ResponseCookie.from(name,value)
                    .domain("")
                    .sameSite("None")
                    .secure(true)
                    .httpOnly(true)
                    .path("/")
                    .maxAge(maxAge)
                    .build();
        }
        response.addHeader("Set-Cookie",cookie1.toString());
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (name.equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
    }

    public static String serialize(Object obj) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(obj));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(
                SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())
                )
        );
    }

}
