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
    public int position_code;

    @Column(nullable = false)
    public String dept_id;

    @Column(nullable = false)
    public boolean is_dept_leader;

    @Builder
    public Member(String name, String email, String password, int position_code, String dept_id, boolean is_dept_leader) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.position_code = position_code;
        this.dept_id = dept_id;
        this.is_dept_leader = is_dept_leader;
    }
}
