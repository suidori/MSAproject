package com.management.user_service.sign;

import com.management.user_service.user.Role;
import com.management.user_service.user.User;
import com.management.user_service.user.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment  environment;

    public void signUp(JoinDto joinDto){
        User user = User.builder()
                .uuid(UUID.randomUUID().toString())
                .userid(joinDto.getUserid())
                .password(passwordEncoder.encode(joinDto.getPassword()))
                .name(joinDto.getName())
                .phoneNumber(joinDto.getPhoneNumber())
                .email(joinDto.getEmail())
                .role(joinDto.getRole())
                .accept(joinDto.getRole() == Role.ROLE_STUDENT)
                .build();

        userRepository.save(user);
    }

    public String signIn(LoginReqDto loginReqDto){
        User user = userRepository.findByUserid(loginReqDto.getUserid())
                .orElseThrow(() -> new UsernameNotFoundException("틀린 아이디"));

        if(!user.getRole().equals(loginReqDto.getRole())){
            throw new BadCredentialsException("올바른 역할로 시도해 주세요.");
        }

        if (passwordEncoder.matches(loginReqDto.getPassword(), user.getPassword())) {
            return generateJwtToken(user);
        } else {
            throw new BadCredentialsException("틀린 비밀번호");
        }
    }

    public String generateJwtToken(User user) {
        String secretKey = environment.getProperty("spring.jwt.secret");

        if (secretKey == null || secretKey.isEmpty()) {
            throw new IllegalArgumentException("JWT secret key is not defined");
        }

        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        long expirationTime = Long.parseLong(environment.getProperty("spring.jwt.expiration", "86400000"));

        return Jwts.builder()
                .subject(user.getUuid())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), Jwts.SIG.HS256)
                .compact();
    }

}
