package com.dongyang.dongpo.config.security;

import com.dongyang.dongpo.exception.member.MemberNotFoundException;
import com.dongyang.dongpo.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return memberRepository.findByEmail(username).orElseThrow(MemberNotFoundException::new);
        } catch (MemberNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
