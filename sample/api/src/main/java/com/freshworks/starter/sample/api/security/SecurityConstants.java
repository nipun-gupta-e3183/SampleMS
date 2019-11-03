package com.freshworks.starter.sample.api.security;

interface SecurityConstants {
    long EXPIRATION_TIME = 60_000; // 1 minute
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}

