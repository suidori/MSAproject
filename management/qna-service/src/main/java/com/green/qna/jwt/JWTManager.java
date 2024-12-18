package com.green.qna.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JWTManager {

    private final Environment environment;

    public Jws<Claims> getClaims(String jwt){
        String secrekey = environment.getProperty("spring.jwt.secret");
        try {
            // 비밀번호 설정
            SecretKey secretKey
                    = new SecretKeySpec(secrekey.getBytes(),
                    Jwts.SIG.HS256.key().build().getAlgorithm());

            // 해당비밀번호로 jwt 토큰 복호화 해서 claims 가져오기
            Jws<Claims> cliams = Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(jwt);

            return cliams;
        }catch (Exception e){
            e.printStackTrace();
//            throw new Exception("JWT TOKEN 문제 = "+e.getMessage());
        }
        return null;
    }

}
