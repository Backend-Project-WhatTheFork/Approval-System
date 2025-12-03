package com.whatthefork.authandsecurity.domain.controller;


import com.whatthefork.authandsecurity.domain.repository.MemberRepository;
import com.whatthefork.authandsecurity.domain.service.MemberService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;

    public MemberController(MemberService memberService, MemberRepository memberRepository) {
        this.memberService = memberService;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/login")
    public boolean login() {

        return false;
    }

}
