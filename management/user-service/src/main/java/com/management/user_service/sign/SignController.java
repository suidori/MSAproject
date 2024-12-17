package com.management.user_service.sign;

import com.management.user_service.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("user/sign")
@RequiredArgsConstructor
public class SignController {

    private final UserRepository userRepository;
    private final SignService signService;
    @GetMapping("/checkid")
    public boolean checkId(@RequestParam("userid") String userid){
        if(userRepository.findByUserid(userid).isPresent()){
            throw new IllegalArgumentException("이미 사용 중인 ID입니다.");
        } else {
            return true;
        }
    }

    @GetMapping("/checkphone")
    public boolean checkPhone(@RequestParam("phoneNumber") String phoneNumber){
        if(userRepository.findByPhoneNumber(phoneNumber).isPresent()){
            throw new IllegalArgumentException("이미 사용 중인 전화번호입니다.");
        } else {
            return true;
        }
    }

    @PostMapping("/signin")
    public ResponseEntity<String> join(@RequestBody JoinDto joinDto){
        signService.signUp(joinDto);
        return ResponseEntity.ok("회원가입 완료");
    }

    @PostMapping("/signup")
    public ResponseEntity<String> login(
            @RequestBody LoginReqDto loginReqDto
    ){
        String token = signService.signIn(loginReqDto);
        return ResponseEntity.ok(token);
    }

}
