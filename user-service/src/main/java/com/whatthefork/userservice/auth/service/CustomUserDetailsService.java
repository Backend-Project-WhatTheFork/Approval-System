package com.whatthefork.userservice.auth.service;

import com.whatthefork.userservice.auth.model.CustomUser;
import com.whatthefork.userservice.command.entity.User;
import com.whatthefork.userservice.command.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("유저 찾지 못함"));

        /* 수정 사항 : spring security의 UserDetails를 extends 하여 CustomUser 작성
        * 고유 id 숫자, 로그인 id(email), password, authorities 를 저장하고 반환
        * */
        return CustomUser.builder()
                .id(user.getId())
                .username(user.getEmail())
                .password(user.getPassword())
                .authorities(Collections.singleton(new SimpleGrantedAuthority(user.getRole().name())))
                .build();
    }
}
