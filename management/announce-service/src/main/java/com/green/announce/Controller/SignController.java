package com.green.announce.Controller;


import com.green.announce.Dto.JoinDto;
import com.green.announce.Dto.LoginReqDto;
import com.green.announce.Entity.User;
import com.green.announce.Repository.UserRepository;
import com.green.announce.Service.SignInService;
import com.green.announce.jwt.JWTManager;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

//@RestController
@RequiredArgsConstructor
//@CrossOrigin
//@RequestMapping("sign")
public class SignController {

    private final SignInService signInService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JWTManager jwtManager;

    @GetMapping("/checkid")
    public boolean checkId(@RequestParam("userid") String userid){
        return userRepository.findByUserid(userid).isEmpty();
    }

    @GetMapping("/checkphone")
    public boolean checkPhone(@RequestParam("phoneNumber") String phoneNumber){
        System.out.println(phoneNumber);
        return userRepository.findByPhoneNumber(phoneNumber).isEmpty();
    }

    @PostMapping("/signin")
    public String join(@RequestBody JoinDto joinDto){
        signInService.join(joinDto);
        return "success";
    }

    @PostMapping("/login")
    public ResponseEntity<String> redirectWithPost(
            @RequestBody LoginReqDto loginReqDto,
            HttpServletResponse response) throws IOException {

        User user = userRepository.findByUserid(loginReqDto.getUserid()).orElseThrow(
                () -> new UsernameNotFoundException(loginReqDto.getUserid())
        );

        boolean isMatch = passwordEncoder.matches(loginReqDto.getPassword(), user.getPassword());
        if(isMatch){
            String jwt = jwtManager.createJWT(user.getUserid(), user.getIdx(), user.getRole().toString());
            return ResponseEntity.ok(jwt);
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("아이디와 패스워드를 확인하세요");
        }
    }


}
