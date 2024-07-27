package com.dongyang.dongpo.TestDataInit;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.MemberTitle;
import com.dongyang.dongpo.domain.member.Title;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.repository.member.MemberRepository;
import com.dongyang.dongpo.repository.member.MemberTitleRepository;
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
    private final MemberTitleRepository memberTitleRepository;

    @Override
    public void run(String... args) throws Exception {
        Member member1 = Member.builder()
                .email("kim@naver.com")
                .name("김철수")
                .nickname("김김김")
                .profilePic(null)
                .mainTitle(Title.BASIC_TITLE)
                .role(Member.Role.ROLE_MEMBER)
                .gender(Member.Gender.GEN_MALE)
                .ageGroup("20~29")
                .socialType(Member.SocialType.NAVER)
                .socialId(null)
                .signupDate(LocalDateTime.now())
                .leaveDate(null)
                .status(Member.Status.ACTIVE)
                .build();
        MemberTitle memberTitle1 = MemberTitle.builder()
                .member(member1)
                .title(member1.getMainTitle())
                .achieveDate(LocalDateTime.now())
                .build();

        Member member2 = Member.builder()
                .email("park@kakao.com")
                .name("박영희")
                .nickname("박박박")
                .profilePic(null)
                .mainTitle(Title.FAILED_TO_VISIT)
                .role(Member.Role.ROLE_MEMBER)
                .gender(Member.Gender.GEN_FEMALE)
                .ageGroup("30~39")
                .socialType(Member.SocialType.KAKAO)
                .socialId(null)
                .signupDate(LocalDateTime.now())
                .leaveDate(null)
                .status(Member.Status.ACTIVE)
                .build();
        MemberTitle memberTitle2 = MemberTitle.builder()
                .member(member2)
                .title(member2.getMainTitle())
                .achieveDate(LocalDateTime.now())
                .build();

        Member member3 = Member.builder()
                .email("lee@kakao.com")
                .name("이기자")
                .nickname("이이이")
                .profilePic(null)
                .mainTitle(Title.REGULAR_CUSTOMER)
                .role(Member.Role.ROLE_MEMBER)
                .gender(Member.Gender.GEN_FEMALE)
                .ageGroup("30~39")
                .socialType(Member.SocialType.KAKAO)
                .socialId(null)
                .signupDate(LocalDateTime.now())
                .leaveDate(null)
                .status(Member.Status.ACTIVE)
                .build();
        MemberTitle memberTitle3 = MemberTitle.builder()
                .member(member3)
                .title(member3.getMainTitle())
                .achieveDate(LocalDateTime.now())
                .build();

        memberRepository.save(member1);
        memberTitleRepository.save(memberTitle1);
        memberRepository.save(member2);
        memberTitleRepository.save(memberTitle2);
        memberRepository.save(member3);
        memberTitleRepository.save(memberTitle3);

        Store store1 = Store.builder()
                .name("역할맥")
                .address("서울시 구로구 고척동")
                .latitude(37.50054492371818)
                .longitude(126.86630168247805)
                .openTime(LocalTime.of(18, 0))
                .closeTime(LocalTime.of(23, 0))
                .member(member1)
                .isToiletValid(true)
                .status(Store.StoreStatus.ACTIVE)
                .build();

        Store store2 = Store.builder()
                .name("시골집")
                .address("서울시 구로구 고척동")
                .latitude(37.50109695453075)
                .longitude(126.86845508548086)
                .openTime(LocalTime.of(16, 0))
                .closeTime(LocalTime.of(21, 0))
                .member(member2)
                .isToiletValid(false)
                .status(Store.StoreStatus.ACTIVE)
                .build();

        Store store3 = Store.builder()
                .name("써브웨이")
                .address("서울시 구로구 고척동")
                .latitude(37.500986658450685)
                .longitude(126.86651576780534)
                .openTime(LocalTime.of(16, 0))
                .closeTime(LocalTime.of(21, 0))
                .member(member3)
                .isToiletValid(true)
                .status(Store.StoreStatus.ACTIVE)
                .build();

        Store store4 = Store.builder()
                .name("구로역앞")
                .address("서울시 구로구 구로동")
                .latitude(37.502763)
                .longitude(126.880547)
                .openTime(LocalTime.of(16, 0))
                .closeTime(LocalTime.of(21, 0))
                .member(member1)
                .isToiletValid(true)
                .status(Store.StoreStatus.ACTIVE)
                .build();

        storeRepository.save(store1);
        storeRepository.save(store2);
        storeRepository.save(store3);
        storeRepository.save(store4);
    }
}
