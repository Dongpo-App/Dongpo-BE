package com.dongyang.dongpo.service.mypage;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.MemberTitle;
import com.dongyang.dongpo.dto.mypage.MyPageDto;
import com.dongyang.dongpo.dto.mypage.MyPageUpdateDto;
import com.dongyang.dongpo.exception.data.ArgumentNotSatisfiedException;
import com.dongyang.dongpo.jwt.JwtTokenProvider;
import com.dongyang.dongpo.repository.member.MemberRepository;
import com.dongyang.dongpo.repository.member.MemberTitleRepository;
import com.dongyang.dongpo.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dongyang.dongpo.exception.member.MemberNotFoundException;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MyPageService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final S3Service s3Service;
    private final MemberTitleRepository memberTitleRepository;

    @Value("${cloud.aws.s3.bucket-full-url}")
    private String bucketFullUrl;

    public MyPageDto getMyPageIndex(String accessToken) throws Exception {
        Member member = memberRepository.findByEmail(jwtTokenProvider.parseClaims(accessToken).getSubject()).orElseThrow(MemberNotFoundException::new);
        List<MemberTitle> memberTitles = memberTitleRepository.findByMember(member);
        return MyPageDto.toEntity(member, memberTitles);
    }

    @Transactional
    public void updateMyPageInfo(String accessToken, MyPageUpdateDto myPageUpdateDto) {
        if (myPageUpdateDto.getNickname().length() > 7) // 문자 수 7자 초과시 예외 발생
            throw new ArgumentNotSatisfiedException();
        memberRepository.findByEmail(jwtTokenProvider.parseClaims(accessToken).getSubject()).ifPresent(member -> {
            if (myPageUpdateDto.getProfilePic() != null && !myPageUpdateDto.getProfilePic().isBlank()) {
                if (member.getProfilePic().startsWith(bucketFullUrl))
                    s3Service.deleteFile(member.getProfilePic()); // S3에 있는 기존 프로필 사진 삭제
                member.updateMemberProfilePic(myPageUpdateDto.getProfilePic());
                log.info("Updated Member profilePic : {}", member.getEmail());
            }
            if (myPageUpdateDto.getNickname() != null && !myPageUpdateDto.getNickname().equals(member.getNickname())) {
                member.updateMemberNickname(myPageUpdateDto.getNickname());
                log.info("Updated Member nickname : {}", member.getEmail());
            }
            if (myPageUpdateDto.getNewMainTitle() != null && !myPageUpdateDto.getNewMainTitle().equals(member.getMainTitle())) {
                MemberTitle memberTitle = memberTitleRepository.findByMemberAndTitle(member, myPageUpdateDto.getNewMainTitle());
                member.updateMemberMainTitle(memberTitle.getTitle());
                log.info("Updated Member mainTitle : {}", member.getEmail());
            }
        });
    }
}
