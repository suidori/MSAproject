package com.management.user_service.sign;

import com.management.user_service.user.User;
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
        return userRepository.findByUserid(userid).isEmpty();
    }

    @GetMapping("/checkphone")
    public boolean checkPhone(@RequestParam("phoneNumber") String phoneNumber){
        return userRepository.findByPhoneNumber(phoneNumber).isEmpty();
    }

    @PostMapping("/signup")
    public ResponseEntity<String> join(@RequestBody JoinDto joinDto){
        signService.signUp(joinDto);
        return ResponseEntity.ok("회원가입 완료");
    }

    @PostMapping("/signin")
    public ResponseEntity<String> login(
            @RequestBody LoginReqDto loginReqDto
    ){
        String token = signService.signIn(loginReqDto);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/getuser")
    public ResponseEntity<User> getUserFromToken(@RequestHeader("Authorization") String token) {
        User user = signService.getUserFromToken(token);
        if (user!=null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(401).body(null);
        }
    }

}
