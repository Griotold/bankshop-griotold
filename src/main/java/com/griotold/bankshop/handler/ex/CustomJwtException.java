package com.griotold.bankshop.handler.ex;

public class CustomJwtException extends RuntimeException{

    public CustomJwtException(String message) {
        super(message);
    }
}
