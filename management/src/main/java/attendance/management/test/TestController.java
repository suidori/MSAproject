package attendance.management.test;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin
@RestController
@RequestMapping("test")
@RequiredArgsConstructor

public class TestController {

    @GetMapping("/checkRole")
    public String checkUserRole(Authentication authentication) {
        System.out.println("Current User: " + authentication.getName());
        System.out.println("Authorities: " + authentication.getAuthorities());
        return "접근 성공! 당신은 STUDENT 역할을 가지고 있습니다.";
    }
}
