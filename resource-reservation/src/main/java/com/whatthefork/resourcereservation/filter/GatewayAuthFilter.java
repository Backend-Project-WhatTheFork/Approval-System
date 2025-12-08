package com.whatthefork.resourcereservation.filter;

// GatewayAuthFilter.java (가장 중요)

import com.whatthefork.resourcereservation.resource.dto.user.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class GatewayAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // 1. 게이트웨이에서 전달된 사용자 정보 헤더 추출
        final String userId = request.getHeader("X-User-ID");
        final String userRoles = request.getHeader("X-User-Roles"); // 예: "ROLE_USER,ROLE_MANAGER"

        // 2. 헤더 정보가 유효하고, Security Context에 인증 정보가 없을 경우 처리
        if (userId != null && userRoles != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 3. 권한 목록 생성 (ROLE_ 접두사 처리 권장)
            List<GrantedAuthority> authorities = Arrays.stream(userRoles.split(","))
                    .map(String::trim)
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            // 4. UserDetails 객체 생성
            UserDetails userDetails = new UserDetailsImpl(userId, authorities);

            // 5. Authentication 객체 생성 (비밀번호 없는 인증 토큰 사용)
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, // Principal: UserDetailsImpl 객체
                    null,        // Credentials: null (이미 게이트웨이에서 인증 완료)
                    authorities  // Authorities: 권한 목록
            );

            // 6. Security Context에 Authentication 객체 등록
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        // 다음 필터로 진행
        filterChain.doFilter(request, response);
    }
}