//package com.green.qna.Filter;
//
//import com.green.qna.Login.LoginUserDetails;
//import com.green.qna.jwt.JWTManager;
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jws;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.springframework.http.HttpHeaders;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//
////@Component
//public class JWTFilter extends OncePerRequestFilter {
//
//
//    private final JWTManager jwtManager;
//
//    public JWTFilter(JWTManager jwtManager) {
//        this.jwtManager = jwtManager;
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain)
//            throws ServletException, IOException {
//        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
//        //  jwt 토큰이 넘어오지 않으면...로그인 처리 안함..
//        if(auth == null || !auth.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//        // jwt 토큰이 넘어 오면...
//        else {
//            try {
//                System.out.println("여기오나");
//                // Bearer eyJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6ImFhYUBuYXZlci5jb20iLCJyb2xlIjoiUk9MRV9BRE1JTiIsImlhdCI6MTcyODYxMjAzNCwiZXhwIjoxNzI4Njk4NDM0fQ.DImwEJGtTTQfm4I2cxiFHV3R3ZxnYiPCl-Vw2MFUkYg
//                String token = auth.split(" ")[1];
//                // token 이 유효한지 검사해서 해당되는 userid, role, useridx 정보들을 가지고 옵니다.
//                Jws<Claims> claims = jwtManager.getClaims(token);
//                String userid = claims.getBody().get("userid").toString();
//                String role = claims.getBody().get("role").toString();
//                Long userIdx = claims.getBody().get("useridx", Long.class);
//                System.out.println(role);
//                LoginUserDetails loginUserDetails = new LoginUserDetails(userid,null, role, userIdx);
//                Authentication authentication = new UsernamePasswordAuthenticationToken(
//                        loginUserDetails, null,
//                        loginUserDetails.getAuthorities()
//                );
//
//                // 로그인설정
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            } catch (Exception e) {
//                e.printStackTrace();
////                System.out.println(e.getMessage());
////                throw new AuthException(e.getMessage());
//            }
//        }
//
//        // freeboard >>>
//        // todo >>>
//        // user >>>
//        filterChain.doFilter(request,response);
//    }
//}
