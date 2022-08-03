package com.authentication.authentication.model;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

public class Token {

    private  String token;

    public static Token of(Long userId,Long validityInMinutes,String secretKey){
        Instant issueDate= Instant.now();
          return new Token(Jwts.builder()
                .claim("userId",userId)
                .setIssuedAt(Date.from(issueDate))
                .setExpiration(Date.from(issueDate.plus(validityInMinutes, ChronoUnit.MINUTES)))
                .signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8)))
                .compact());
    }
    public static Token of(String token){
        return new Token(token);
    }
    private Token(String token) {
        this.token = token;
    }

    public static Long from(String token, String accessTokenSecret) {
     return ((Claims)Jwts.parserBuilder()
     .setSigningKey(Base64.getEncoder().encodeToString(accessTokenSecret.getBytes(StandardCharsets.UTF_8)))
     .build()
     .parse(token)
     .getBody()).get("user_id",Long.class);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
