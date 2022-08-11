package com.A108.Watchme.oauth.exception;

import com.A108.Watchme.DTO.ErrorDTO;
import com.A108.Watchme.Exception.CustomException;
import com.A108.Watchme.Exception.ErrorResponse;
import com.A108.Watchme.Http.Code;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException
    ) throws IOException, ServletException {
//        authException.printStackTrace();
        log.info("Responding with unauthorized error. Message := {}", authException.getMessage());
//        response.sendError(
//                HttpServletResponse.SC_UNAUTHORIZED,
//                authException.getLocalizedMessage()
//        );
//        throw new CustomException(Code.C502);
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().println("{ \"code\" : \"" + Code.C502.getErrCode()
                + "\", \"message\" : \"" +  Code.C502.getMessage() + "  \""
                + "}");
    }
}
