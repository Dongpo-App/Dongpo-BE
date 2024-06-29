package com.dongyang.dongpo.TestDataInit;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.repository.member.MemberRepository;
import com.dongyang.dongpo.repository.store.StoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class MemberDataInit implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;

    @Override
    public void run(String... args) throws Exception {
        Member member1 = Member.builder()
                .email("kim@naver.com")
                .name("김철수")
                .nickname("김김김")
                .profilePic(null)
                .role(Member.Role.ROLE_MEMBER)
                .gender(Member.Gender.GEN_MALE)
                .ageGroup("20~29")
                .socialType(Member.SocialType.NAVER)
                .socialId(null)
                .signupDate(LocalDateTime.now())
                .leaveDate(null)
                .status(Member.Status.ACTIVE)
                .build();

        Member member2 = Member.builder()
                .email("park@kakao.com")
                .name("박영희")
                .nickname("박박박")
                .profilePic(null)
                .role(Member.Role.ROLE_MEMBER)
                .gender(Member.Gender.GEN_FEMALE)
                .ageGroup("30~39")
                .socialType(Member.SocialType.KAKAO)
                .socialId(null)
                .signupDate(LocalDateTime.now())
                .leaveDate(null)
                .status(Member.Status.ACTIVE)
                .build();

        Member member3 = Member.builder()
                .email("lee@kakao.com")
                .name("이기자")
                .nickname("이이이")
                .profilePic(null)
                .role(Member.Role.ROLE_MEMBER)
                .gender(Member.Gender.GEN_FEMALE)
                .ageGroup("30~39")
                .socialType(Member.SocialType.KAKAO)
                .socialId(null)
                .signupDate(LocalDateTime.now())
                .leaveDate(null)
                .status(Member.Status.ACTIVE)
                .build();

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        Store store1 = Store.builder()
                .name("맛집1")
                .address("서울시 강남구")
                .latitude(37.495470)
                .longitude(126.887639)
                .openTime(LocalTime.of(18, 0))
                .closeTime(LocalTime.of(23, 0))
                .member(member1)
                .isToiletValid(true)
                .status(Store.StoreStatus.ACTIVE)
                .build();

        storeRepository.save(store1);

    }
}
