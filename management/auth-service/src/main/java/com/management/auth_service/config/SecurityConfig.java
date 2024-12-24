package com.management.auth_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())  // CSRF 보호 비활성화
                .formLogin(form -> form.disable())  // 폼 로그인 비활성화
                .httpBasic(basic -> basic.disable())  // HTTP Basic 인증 비활성화

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/**").permitAll()  // 로그인과 검증은 누구나 접근 가능
                        .anyRequest().authenticated()  // 나머지 요청은 인증 필요
                );

        return http.build();
    }
}
