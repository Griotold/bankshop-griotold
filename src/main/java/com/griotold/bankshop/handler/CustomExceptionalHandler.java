package com.griotold.bankshop.handler;

import com.griotold.bankshop.dto.ResponseDto;
import com.griotold.bankshop.handler.ex.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class CustomExceptionalHandler {
    @ExceptionHandler(CustomJwtException.class)
    public ResponseEntity<?> expiredJwt(CustomForbiddenException e) {
        log.error("CustomJwtException = {}", e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CustomForbiddenException.class)
    public ResponseEntity<?> forbiddenException(CustomForbiddenException e) {
        log.error("CustomForbiddenException = {}", e.getMessage());
        return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(CustomOutOfStockException.class)
    public ResponseEntity<?> outOfStockException(CustomOutOfStockException e) {
        log.error("CustomOutOfStockException = {}", e.getMessage());
        return new ResponseEntity<>(
                new ResponseDto<>(-1, e.getMessage(), null),
                HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(CustomApiException.class)
    public ResponseEntity<?> apiException(CustomApiException e) {
        log.error("CustomApiException = {}", e.getMessage());
        return new ResponseEntity<>(
                new ResponseDto<>(-1, e.getMessage(), null),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<?> validationApiException(CustomValidationException e) {
        log.error("CustomValidationException = {}", e.getMessage());
        return new ResponseEntity<>(
                new ResponseDto<>(-1, e.getMessage(), e.getErrorMap()),
                        HttpStatus.BAD_REQUEST);
    }
}
