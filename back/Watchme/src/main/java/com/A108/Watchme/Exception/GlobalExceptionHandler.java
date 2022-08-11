package com.A108.Watchme.Exception;

import com.A108.Watchme.DTO.ErrorDTO;
import com.A108.Watchme.Http.Code;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler({CustomException.class})
    protected ResponseEntity<Object> handleCustomException(CustomException ex) {
        Code code = ex.getCode();
        return handleExceptionInternal(code);
    }

    private ResponseEntity<Object> handleExceptionInternal(Code code) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(makeErrorResponse(code));
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllException(Exception ex) {
        Code errorCode = Code.C500;
        return handleExceptionInternal(errorCode);
    }
    @ExceptionHandler(AuthenticationException.class)
    @ResponseBody
    public ResponseEntity<ErrorResponse> handleAuthenticaitionException(AuthenticationException ex){
        ErrorResponse errorResponse = new ErrorResponse(ex.getErrorCode().getCode(), ex.getErrorCode().getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({ExpiredJwtException.class})
    public ResponseEntity<Object> handleExpiredJwtException(Exception ex) {
        Code errorCode = Code.C501;
        return handleExceptionInternal(errorCode);
    }

    private ErrorDTO makeErrorResponse(Code errorCode) {
        return ErrorDTO.builder()
                .code(errorCode.getErrCode())
                .message(errorCode.getMessage())
                .build();
    }


}
