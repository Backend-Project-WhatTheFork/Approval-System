package com.whatthefork.authandsecurity.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private int positionCode;
    private String deptId;
    private boolean isDeptLeader;
    private boolean isAdmin;
}