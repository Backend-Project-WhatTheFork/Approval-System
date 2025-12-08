package com.whatthefork.authandsecurity.domain.service;

import com.whatthefork.authandsecurity.domain.model.Member;
import com.whatthefork.authandsecurity.domain.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    public Member getMemberById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));
    }

    public Member updateMember(Long id, Member memberDetails) {
        Member member = getMemberById(id);

        String updatedPassword = (memberDetails.getPassword() != null && !memberDetails.getPassword().isEmpty())
                ? passwordEncoder.encode(memberDetails.getPassword())
                : member.getPassword();

        Member updatedMember = new Member(
                id,
                memberDetails.getName(),
                memberDetails.getEmail(),
                updatedPassword,
                memberDetails.getPositionCode(),
                memberDetails.getDeptId(),
                memberDetails.isDeptLeader(),
                memberDetails.isAdmin());

        return memberRepository.save(updatedMember);
    }

    public void deleteMember(Long id) {
        Member member = getMemberById(id);
        memberRepository.delete(member);
    }
}