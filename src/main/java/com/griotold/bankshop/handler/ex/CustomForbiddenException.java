package com.griotold.bankshop.handler.ex;

public class CustomForbiddenException extends RuntimeException {
    public CustomForbiddenException(String message) {
        super(message);
    }

    public CustomForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }
}
