package com.whatthefork.resourcereservation.resource.config;

import com.whatthefork.resourcereservation.resource.filter.GatewayAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true) // @PreAuthorize 활성화
public class SecurityConfig {

    // 1. 커스텀 필터를 빈으로 등록 (빈 등록은 필요 없을 수도 있음. SecurityFilterChain 내부에서 인스턴스화 가능)

    // 2. 필터 체인 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // CSRF 보호 비활성화 (API 서버이므로)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 세션 미사용
                        .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/conference-room/**", "/corporate-cars/**", "/supplies/**", "/reservations/**").permitAll()
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-resources/**"
                        ).permitAll()
                        .anyRequest().authenticated()
                        )
                .addFilterBefore(new GatewayAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        // 3. 커스텀 필터를 UsernamePasswordAuthenticationFilter 이전에 추가
        http.addFilterBefore(new com.whatthefork.resourcereservation.resource.filter.GatewayAuthFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}