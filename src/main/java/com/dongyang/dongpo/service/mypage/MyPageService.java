package com.dongyang.dongpo.service.mypage;

import com.dongyang.dongpo.dto.mypage.MyPageDto;
import com.dongyang.dongpo.dto.mypage.MyPageUpdateDto;
import com.dongyang.dongpo.jwt.JwtTokenProvider;
import com.dongyang.dongpo.repository.member.MemberRepository;
import com.dongyang.dongpo.s3.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dongyang.dongpo.exception.member.MemberNotFoundException;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MyPageService {

    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final S3Service s3Service;

    @Value("${cloud.aws.s3.bucket-full-url}")
    private String bucketFullUrl;

    public MyPageDto getMyPageIndex(String accessToken) throws Exception {
        return MyPageDto.toEntity(memberRepository.findByEmail(jwtTokenProvider.parseClaims(accessToken).getSubject()).orElseThrow(MemberNotFoundException::new));
    }

    @Transactional
    public void updateMyPageInfo(String accessToken, MyPageUpdateDto myPageUpdateDto) {
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
        });
    }
}
