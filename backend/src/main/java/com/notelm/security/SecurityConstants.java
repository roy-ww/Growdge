package com.notelm.security;

public class SecurityConstants {
    public static final String JWT_SECRET = "mySecretKeyForNoteLMApplication";
    public static final int JWT_EXPIRATION = 86400000; // 24小时
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}