package com.management.auth_service.utils;

import io.jsonwebtoken.*;
import java.security.*;
import java.util.Date;

public class JwtUtil {

    private static final long EXPIRATION_TIME = 1000 * 60 * 60 * 24; // 24시간

    // JWT 생성
    public static String generateToken(String uuid, String role) throws Exception {
        PrivateKey privateKey = KeyUtil.getPrivateKey();



        return Jwts.builder()
                .subject(uuid) // 사용자 식별값
                .claim("role", role)  // 사용자 역할 추가
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // 만료 시간
                .signWith(privateKey, Jwts.SIG.RS256) // RS256 서명
                .compact();
    }

    // JWT 검증
    public static Claims validateToken(String token) throws Exception {
        PublicKey publicKey = KeyUtil.getPublicKey();

        return Jwts.parser()
                .verifyWith(publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
