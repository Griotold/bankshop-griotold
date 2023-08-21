package com.griotold.bankshop.handler.ex;

public class CustomOutOfStockException extends RuntimeException{

    public CustomOutOfStockException(String message) {
        super(message);
    }
}
