package com.whatthefork.authandsecurity.domain.service;

import com.whatthefork.authandsecurity.domain.dto.LoginRequest;
import com.whatthefork.authandsecurity.domain.dto.LoginResponse;
import com.whatthefork.authandsecurity.domain.dto.RegisterRequest;
import com.whatthefork.authandsecurity.domain.model.Member;
import com.whatthefork.authandsecurity.domain.repository.MemberRepository;
import com.whatthefork.authandsecurity.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(MemberRepository memberRepository, PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtUtil.generateToken(member.getEmail());

        return new LoginResponse(token, member.getEmail(), member.getName(),
                member.isAdmin(), member.isDeptLeader());
    }

    public Member register(RegisterRequest request) {
        if (memberRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        Member member = Member.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .positionCode(request.getPositionCode())
                .deptId(request.getDeptId())
                .isDeptLeader(request.isDeptLeader())
                .isAdmin(request.isAdmin())
                .build();

        return memberRepository.save(member);
    }
}