package com.dongyang.dongpo.service.member;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.Role;
import com.dongyang.dongpo.domain.member.SocialType;
import com.dongyang.dongpo.jwt.JwtTokenProvider;
import com.dongyang.dongpo.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SignService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;


    @Transactional
    public void SignUp(String email){ // 임시
        Member member = Member.builder()
                .email(email)
                .signup_date(LocalDateTime.now())
                .socialType(SocialType.KAKAO)
                .role(Role.ROLE_MEMBER)
                .build();
        memberRepository.save(member);
    }

//    public void SignIn(String email) {
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email);
//        Member member = memberRepository.findByEmail(email).orElseThrow(NoSuchElementException::new);
//
//        jwtTokenProvider.createToken(email, Role.ROLE_MEMBER);
//    }
}
