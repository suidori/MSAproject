package attendance.management.user;

import attendance.management.error.BizException;
import attendance.management.error.ErrorCode;
import attendance.management.sign.LoginUserDetails;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin
@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {
    private final UserRepository userRepository;
    private final UserService userService;

    @GetMapping("/roles")
    public String getUserRoles() {
        // 현재 인증된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 사용자가 가지고 있는 권한(Role) 목록 가져오기
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        // Role 목록을 문자열로 변환하여 출력
        StringBuilder roles = new StringBuilder("User Roles: ");
        for (GrantedAuthority authority : authorities) {
            roles.append(authority.getAuthority()).append(" ");
        }

        return roles.toString();
    }

    @GetMapping("/getuser")
    public ResponseEntity<User> getUser(
            @AuthenticationPrincipal LoginUserDetails loginUserDetails
    ) {
        if (loginUserDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(userRepository.findByIdx(loginUserDetails.getIdx()).get());
    }

    @GetMapping("/getrole")
    public ResponseEntity<Role> getRole(
            @AuthenticationPrincipal LoginUserDetails loginUserDetails
    ) {
        if (loginUserDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Role role = userRepository.findByIdx(loginUserDetails.getIdx()).orElseThrow(()->{throw new BizException(ErrorCode.USER_NOT_FOUND);}).getRole();

        return ResponseEntity.ok(role);
    }

}
