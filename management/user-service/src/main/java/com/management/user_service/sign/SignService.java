package com.management.user_service.sign;

import com.management.user_service.user.Role;
import com.management.user_service.user.User;
import com.management.user_service.user.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final Environment  environment;

    public void signUp(JoinDto joinDto){
        User user = User.builder()
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

    private String generateJwtToken(User user) {
        String secretkey = environment.getProperty("spring.jwt.secret");
        System.out.println(secretkey);
        return Jwts.builder()
                .claim("userid", user.getUserid())
                .claim("useridx", user.getIdx())
                .claim("role", user.getRole().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(SignatureAlgorithm.HS256, secretkey)
                .compact();
    }
}
