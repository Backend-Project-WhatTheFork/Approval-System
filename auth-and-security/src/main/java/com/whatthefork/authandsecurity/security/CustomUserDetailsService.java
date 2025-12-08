package com.whatthefork.authandsecurity.security;

import com.whatthefork.authandsecurity.domain.model.Member;
import com.whatthefork.authandsecurity.domain.repository.MemberRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    public CustomUserDetailsService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (member.isAdmin()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }
        if (member.isDeptLeader()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_DEPT_LEADER"));
        }
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        return new User(member.getEmail(), member.getPassword(), authorities);
    }
}