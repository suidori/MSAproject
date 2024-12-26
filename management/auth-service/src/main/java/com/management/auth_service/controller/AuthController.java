package com.management.auth_service.controller;

import com.management.auth_service.utils.JwtUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/generate")
    public String login(@RequestParam String uuid, @RequestParam String role) throws Exception {
        // 사용자 이름과 역할로 JWT 생성
        return JwtUtil.generateToken(uuid, role);
    }

    @GetMapping("/validate")
    public String validate(@RequestHeader("Authorization") String token) throws Exception {

        if (token.startsWith("Bearer ")) {
            token = token.substring(7);
        }

        try {
            return JwtUtil.validateToken(token).getSubject();
        } catch (Exception e) {
            return "errorToken" + e.getMessage();
        }

    }

}

