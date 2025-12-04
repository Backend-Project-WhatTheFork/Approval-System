package com.whatthefork.authandsecurity.domain.controller;

import com.whatthefork.authandsecurity.domain.dto.LoginRequest;
import com.whatthefork.authandsecurity.domain.dto.LoginResponse;
import com.whatthefork.authandsecurity.domain.dto.RegisterRequest;
import com.whatthefork.authandsecurity.domain.model.Member;
import com.whatthefork.authandsecurity.domain.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        try {
            LoginResponse response = authService.login(request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(401).build();
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Member> register(@RequestBody RegisterRequest request) {
        try {
            Member member = authService.register(request);
            return ResponseEntity.ok(member);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}