package com.whatthefork.authandsecurity.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String email;
    private String name;
    private boolean isAdmin;
    private boolean isDeptLeader;
}