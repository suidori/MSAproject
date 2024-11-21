package attendance.management.sign;

import attendance.management.jwt.JWTManager;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTManager jwtManager;

    public LoginFilter(AuthenticationManager authenticationManager,
                       JWTManager jwtManager) {
        // post login 들어오면..
        this.setFilterProcessesUrl("/login");
        // UserDetailsSerivce loadByUsername(String username);
        this.authenticationManager = authenticationManager;
        // jwt 토큰 만들고 jwt 유효성검사하고..
        this.jwtManager = jwtManager;
    }

    // 로그인 시도
    @Override
    public Authentication attemptAuthentication(
                HttpServletRequest request,
                HttpServletResponse response) throws AuthenticationException {

        String userid = request.getParameter("userid");
        String password = request.getParameter("password");

        UsernamePasswordAuthenticationToken token =
                new UsernamePasswordAuthenticationToken(userid,password);
        return authenticationManager.authenticate(token);
    }

    // 로그인 성공... jwt 토큰발행
    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {
        UserDetails userDetails = (UserDetails) authResult.getPrincipal();
        String role = "";
        for (var auth : userDetails.getAuthorities()) {
            role = auth.getAuthority();
        }
        String jwt = jwtManager.createJWT(userDetails.getUsername(), ((LoginUserDetails) userDetails).getIdx(), role);
        response.getWriter().write(jwt);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter()
                .write("check userid and password");
    }
}
