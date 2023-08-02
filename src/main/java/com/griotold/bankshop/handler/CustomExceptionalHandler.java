package com.griotold.bankshop.handler;

import com.griotold.bankshop.dto.ResponseDto;
import com.griotold.bankshop.handler.ex.CustomApiException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.concurrent.CancellationException;

@RestControllerAdvice
@Slf4j
public class CustomExceptionalHandler {

    @ExceptionHandler(CancellationException.class)
    public ResponseEntity<?> apiException(CustomApiException e) {
        log.error(e.getMessage());
        return new ResponseEntity<>(
                new ResponseDto<>(-1, e.getMessage(), null),
                HttpStatus.BAD_REQUEST);
    }
}
