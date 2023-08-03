package com.griotold.bankshop.config.jwt;

public interface JwtVO {
    public static final String SECRET = "CIEL";
    public static final int EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER = "Authorization";

}
