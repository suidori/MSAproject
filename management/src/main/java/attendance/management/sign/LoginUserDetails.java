package attendance.management.sign;

import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@ToString
public class LoginUserDetails implements UserDetails {

    private final String username;
    private final String password;
    private final String role;
    private final long idx;

    public LoginUserDetails(String username, String password, String role, long idx) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.idx = idx;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // "ROLE_" 접두사가 없을 경우에만 붙여줌
        String prefixedRole = this.role.startsWith("ROLE_") ? this.role : "ROLE_" + this.role;
        return List.of(() -> prefixedRole);
    }


    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public Long getIdx() {
        return idx; // IDX 반환 메서드 추가
    }

}
