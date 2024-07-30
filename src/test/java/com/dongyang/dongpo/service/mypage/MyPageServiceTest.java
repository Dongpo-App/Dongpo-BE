package com.dongyang.dongpo.service.mypage;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.MemberTitle;
import com.dongyang.dongpo.domain.member.Title;
import com.dongyang.dongpo.domain.store.Store;
import com.dongyang.dongpo.dto.mypage.MyPageDto;
import com.dongyang.dongpo.dto.mypage.MyPageUpdateDto;
import com.dongyang.dongpo.repository.member.MemberRepository;
import com.dongyang.dongpo.repository.member.MemberTitleRepository;
import com.dongyang.dongpo.repository.store.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@Rollback
class MyPageServiceTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberTitleRepository memberTitleRepository;

    @Autowired
    private StoreRepository storeRepository;

    @BeforeEach
    void setUp() {
        Member member = Member.builder()
                .email("test@example.com")
                .name("테스트")
                .nickname("테스터")
                .profilePic(null)
                .mainTitle(Title.BASIC_TITLE)
                .role(Member.Role.ROLE_MEMBER)
                .build();
        memberRepository.save(member);

        MemberTitle memberTitle1 = MemberTitle.builder()
                .member(member)
                .title(Title.REGULAR_CUSTOMER)
                .achieveDate(LocalDateTime.now())
                .build();
        MemberTitle memberTitle2 = MemberTitle.builder()
                .member(member)
                .title(Title.BASIC_TITLE)
                .achieveDate(LocalDateTime.now())
                .build();
        memberTitleRepository.save(memberTitle1);
        memberTitleRepository.save(memberTitle2);

        Store store1 = Store.builder()
                .name("역할맥")
                .address("서울시 구로구 고척동")
                .latitude(37.50054492371818)
                .longitude(126.86630168247805)
                .openTime(LocalTime.of(18, 0))
                .closeTime(LocalTime.of(23, 0))
                .member(member)
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
                .member(member)
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
                .member(member)
                .isToiletValid(true)
                .status(Store.StoreStatus.ACTIVE)
                .build();

        storeRepository.save(store1);
        storeRepository.save(store2);
        storeRepository.save(store3);
    }

    @Test
    void getMyPageIndex() {
        // given
        Member memberByEmail = memberRepository.findByEmail("test@example.com").orElse(null);

        // when
        List<MemberTitle> memberTitles = memberTitleRepository.findByMember(memberByEmail);
        List<Store> memberStores = storeRepository.findByMember(memberByEmail);
        MyPageDto myPageDto = MyPageDto.toEntity(memberByEmail, memberTitles, memberStores);

        // then
        System.out.println("myPageDto = " + myPageDto.toString());
        assertThat(memberByEmail).isNotNull();
        assertEquals(memberByEmail.getEmail(), "test@example.com");
        assertThat(memberTitles).isNotEmpty();
    }

    @Test
    void updateMyPageInfo() {
        // given
        Member member = memberRepository.findByEmail("test@example.com").orElse(null);
        System.out.println("member = " + member.getNickname());

        MyPageUpdateDto myPageUpdateDto = MyPageUpdateDto.builder()
                .nickname("테스터2")
                .profilePic("https://example.com/profile.jpg")
                .newMainTitle(Title.REGULAR_CUSTOMER)
                .build();

        // when
        if (myPageUpdateDto.getProfilePic() != null && !myPageUpdateDto.getProfilePic().isBlank()) {
            member.setProfilePic(myPageUpdateDto.getProfilePic());
        }
        if (myPageUpdateDto.getNickname() != null && !myPageUpdateDto.getNickname().equals(member.getNickname())) {
            member.setNickname(myPageUpdateDto.getNickname());
        }
        if (myPageUpdateDto.getNewMainTitle() != null && !myPageUpdateDto.getNewMainTitle().equals(member.getMainTitle())) {
            MemberTitle memberTitle = memberTitleRepository.findByMemberAndTitle(member, myPageUpdateDto.getNewMainTitle());
            member.setMainTitle(memberTitle.getTitle());
        }

        Member foundMember = memberRepository.findByEmail("test@example.com").orElse(null);

        // then
        System.out.println("foundMember = " + foundMember.getNickname());
        assertThat(foundMember.getNickname()).isEqualTo("테스터2");
        assertThat(foundMember.getProfilePic()).isEqualTo("https://example.com/profile.jpg");
        assertThat(foundMember.getMainTitle()).isEqualTo(Title.REGULAR_CUSTOMER);
    }
}
