package com.A108.Watchme.Exception;

import com.A108.Watchme.DTO.ErrorDTO;
import com.A108.Watchme.Http.Code;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.naming.LimitExceededException;
import javax.naming.SizeLimitExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({CustomException.class})
    protected ResponseEntity<Object> handleCustomException(CustomException ex) {
        logger.info(ex);
        Code code = ex.getCode();
        return handleExceptionInternal(code);
    }

    private ResponseEntity<Object> handleExceptionInternal(Code code) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(makeErrorResponse(code));
    }
    @ExceptionHandler({MaxUploadSizeExceededException.class})
    protected  ResponseEntity<Object> handleSizeLimitExceeded(MaxUploadSizeExceededException exception){
        return handleExceptionInternal(Code.C529);
    }
    @ExceptionHandler({AuthenticationException.class})
    @ResponseBody
    public ResponseEntity<Object> handleAuthenticaitionException(AuthenticationException ex){
        return handleExceptionInternal(Code.C502);
    }

    @ExceptionHandler({InternalAuthenticationServiceException.class})
    @ResponseBody
    public ResponseEntity<Object> handleAuthenticaitionException(InternalAuthenticationServiceException ex){
        return handleExceptionInternal(Code.C502);
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<Object> handleAllException(Exception ex) {
        ex.printStackTrace();
        Code errorCode = Code.C500;
        return handleExceptionInternal(errorCode);
    }


//
//    @ExceptionHandler({ExpiredJwtException.class})
//    public ResponseEntity<Object> handleExpiredJwtException(Exception ex) {
//        Code errorCode = Code.C500;
//        return handleExceptionInternal(errorCode);
//    }

    private ErrorDTO makeErrorResponse(Code errorCode) {
        return ErrorDTO.builder()
                .code(errorCode.getErrCode())
                .message(errorCode.getMessage())
                .build();
    }


}
