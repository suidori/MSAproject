package attendance.management.jwt;

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

    // JWT 생성
    public String createJWT(String userid, long useridx, String role) {
        String secrekey = environment.getProperty("spring.jwt.secret");
        String jwt = Jwts.builder()
                .claim("userid", userid)
                .claim("useridx", useridx)
                .claim("role", role)
                .setIssuedAt(new Date(System.currentTimeMillis())) // 현재 시간 넣기
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 1초*60*60*24 1일 유효함
                .signWith(SignatureAlgorithm.HS256,
                        Base64.getEncoder().encodeToString(secrekey.getBytes()))
                .compact();
        return jwt;
    }

    // JWT 유효한지 검사 .... 우리가 설정한 비밀번호까지...
    public String validJWT(String jwt, String secrekey) {
        try {
            SecretKey secretKey = new SecretKeySpec(secrekey.getBytes(), SignatureAlgorithm.HS256.getJcaName());

            // JWT 파싱 및 검증 (JJWT 0.9.1 버전에서는 parser() 사용)
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(jwt);

            // 만약에 유효시간이 지났으면... JWT 사용 못하게 하기 위한 구문...
            if (claims.getBody().getExpiration().before(new Date())) {
                return "fail";  // 만료된 경우
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return "fail";
        }
        return "success";  // 유효한 경우
    }

    public Jws<Claims> getClaims(String jwt) {
        String secrekey = environment.getProperty("spring.jwt.secret");
        try {
            // 비밀 키 설정
            SecretKey secretKey = new SecretKeySpec(secrekey.getBytes(), SignatureAlgorithm.HS256.getJcaName());

            // JWT 토큰 복호화하여 Claims 가져오기
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey) // 여기서 비밀 키 설정
                    .parseClaimsJws(jwt); // JWT를 파싱하여 Claims 가져오기

            return claims;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Long extractUserIdxFromToken(String token) {
        String jwtToken = token.replace("Bearer ", "");

        Jws<Claims> claims = Jwts.parser()
                .setSigningKey(Base64.getEncoder().encodeToString("mySuperSecretKey".getBytes()))
                .parseClaimsJws(jwtToken);

        return claims.getBody().get("useridx", Long.class);
    }

}
