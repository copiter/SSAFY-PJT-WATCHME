package com.A108.Watchme.auth;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.security.core.Authentication;

import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
@Component
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
    private HttpSession httpSession;



    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        System.out.println(objectMapper.convertValue(authentication.getDetails(), OAuthUser.class).getEmail());
//        System.out.println(objectMapper.convertValue(authentication.getDetails(), OAuthUser.class).getName());
//        System.out.println(objectMapper.convertValue(authentication.getDetails(), OAuthUser.class).getProviderType());
        AuthDetails authDetails = (AuthDetails) authentication.getPrincipal();
        httpSession = request.getSession();
        httpSession.setAttribute("email",authDetails.getAttributes().get("email").toString());
        httpSession.setAttribute("providerType",authDetails.getProviderType());
        httpSession.setAttribute("image",authDetails.getImgUrl());
        response.sendRedirect("https://watchme2.shop/slogin");
    }
}