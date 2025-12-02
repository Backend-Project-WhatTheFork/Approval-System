package com.whatthefork.authandsecurity.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @Column(nullable = false)
    public String name;
    @Column(nullable = false)
    public String email;

    @Column(nullable = false)
    public String password;

    @Column(nullable = false)
    public int positionCode;

    @Column(nullable = false)
    public String deptId;

    @Column(nullable = false)
    public boolean isDeptLeader;

    @Builder

    public Member(String name, String email, String password, int positionCode, String deptId, boolean isDeptLeader) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.positionCode = positionCode;
        this.deptId = deptId;
        this.isDeptLeader = isDeptLeader;
    }
}
