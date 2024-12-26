package com.management.user_service.sign;

import com.management.user_service.user.Role;
import com.management.user_service.user.User;
import com.management.user_service.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RestTemplate restTemplate;

    public String generateToken(String uuid, String role) {

        // auth-service의 엔드포인트 설정
        String authServiceUrl = "http://auth-service.msa-namespace.svc.cluster.local:9000/auth/generate";

        // HTTP 헤더 설정
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED); // x-www-form-urlencoded 형식 사용

        // 요청 본문에 파라미터 추가
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("uuid", uuid);
        body.add("role", role);

        // HttpEntity 생성 (헤더 + 바디)
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        try {
            // auth-service로 POST 요청 전송
            ResponseEntity<String> response = restTemplate.exchange(
                    authServiceUrl, HttpMethod.POST, entity, String.class);

            // 결과 반환
            return response.getBody();
        } catch (Exception e) {
            // 예외 처리
            return "errorGenerate: " + e.getMessage();
        }
    }


    public String validateToken(String token) {
        String authServiceUrl = "http://auth-service.msa-namespace.svc.cluster.local:9000/auth/validate";

        // 헤더에 Authorization 추가
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        // HttpEntity에 헤더와 빈 바디를 추가
        HttpEntity<String> entity = new HttpEntity<>(headers);

        try {
            // auth-service에서 토큰 검증 결과 받기
            return restTemplate.exchange(authServiceUrl, HttpMethod.GET, entity, String.class).getBody();
        } catch (Exception e) {
            // 예외 처리 (예: 토큰 검증 실패)
            return "errorValidate: " + e.getMessage();
        }
    }

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

    public String signIn(LoginReqDto loginReqDto) {

        try {
            User user = userRepository.findByUserid(loginReqDto.getUserid())
                    .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디"));

            if (!user.getRole().equals(loginReqDto.getRole())) {
                throw new BadCredentialsException("올바른 역할로 시도해 주세요.");
            }

            if (!passwordEncoder.matches(loginReqDto.getPassword(), user.getPassword())) {
                throw new BadCredentialsException("틀린 비밀번호");
            }

            String getToken = generateToken(user.getUuid(), user.getRole().toString());
            return getToken;

        }catch (Exception e) {
            return e.getMessage();
        }

    }

    public User getUserFromToken(String token){

        String validation = validateToken(token);

        System.out.println(validation);

        if(validation.startsWith("error")){
            User sans = userRepository.findById(0L).orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));
            return sans;
        }

        User whoami = userRepository
                .findByUuid(validation)
                .orElseThrow(() -> new NoSuchElementException("존재하지 않는 유저입니다."));

        return whoami;

    }

}
