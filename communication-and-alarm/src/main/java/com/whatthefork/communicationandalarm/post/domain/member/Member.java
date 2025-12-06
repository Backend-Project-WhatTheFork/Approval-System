package com.whatthefork.communicationandalarm.post.domain.member;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Member {

    private long id;

    private String name;

    private String email;

    private String password;

    private int positionCode;

    private String deptId;

    private boolean isDeptLeader;

    private boolean isAdmin;

    @Builder
    public Member(String name, String email, String password, int positionCode, String deptId, boolean isDeptLeader, boolean isAdmin){
        this.name = name;
        this.email = email;
        this.password = password;
        this.positionCode = positionCode;
        this.deptId = deptId;
        this.isDeptLeader = isDeptLeader;
        this.isAdmin = isAdmin;
    }
}
