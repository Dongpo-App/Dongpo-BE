package com.dongyang.dongpo.service.member;

import com.dongyang.dongpo.domain.RefreshToken;
import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.MemberTitle;
import com.dongyang.dongpo.dto.auth.JwtToken;
import com.dongyang.dongpo.dto.auth.UserInfo;
import com.dongyang.dongpo.dto.mypage.MyPageDto;
import com.dongyang.dongpo.dto.mypage.MyPageUpdateDto;
import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
import com.dongyang.dongpo.jwt.JwtTokenProvider;
import com.dongyang.dongpo.repository.member.MemberRepository;
import com.dongyang.dongpo.repository.RefreshTokenRepository;
import com.dongyang.dongpo.repository.member.MemberTitleRepository;
import com.dongyang.dongpo.s3.S3Service;
import com.dongyang.dongpo.service.store.StoreService;
import com.dongyang.dongpo.service.token.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final TokenService tokenService;
    private final MemberTitleRepository memberTitleRepository;
    private final StoreService storeService;
    private final S3Service s3Service;

    @Value("${cloud.aws.s3.bucket-full-url}")
    private String bucketFullUrl;

    @Transactional
    public JwtToken socialSave(UserInfo userInfo){
        Member member = Member.toEntity(userInfo);

        // 이미 가입된 회원인 경우
        if (memberRepository.existsBySocialId(member.getSocialId())) {
            Member existingMember = memberRepository.findBySocialId(member.getSocialId())
                    .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

            // 탈퇴한 회원인 경우
            if (existingMember.getStatus() == Member.Status.LEAVE)
                throw new CustomException(ErrorCode.MEMBER_ALREADY_LEFT);

            // 로그인 처리
            return tokenService.createTokenForLoginMember(existingMember);
        }

        // 신규 가입 회원의 이메일이 중복될 경우 예외 발생
        if (memberRepository.existsByEmail(member.getEmail()))
            throw new CustomException(ErrorCode.MEMBER_EMAIL_DUPLICATED);

        memberRepository.save(member);
        memberTitleRepository.save(MemberTitle.builder()
                .member(member)
                .title(member.getMainTitle())
                .achieveDate(LocalDateTime.now())
                .build());

        JwtToken jwtToken = jwtTokenProvider.createToken(member.getEmail(), member.getRole());
        RefreshToken refreshToken = RefreshToken.builder()
                .email(member.getEmail())
                .refreshToken(jwtToken.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        log.info("Registered Member {} successfully / id: {}", member.getEmail(), member.getId());
        return jwtToken;
    }

    public List<Member> findAll(){
        return memberRepository.findAll();
    }

    public Member findOne(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    public MyPageDto getMemberInfoIndex(Member member) {
        List<MemberTitle> memberTitles = memberTitleRepository.findByMember(member);
        Long storeRegisterCount = storeService.getMyRegisteredStoreCount(member);
        return MyPageDto.toEntity(member, memberTitles, storeRegisterCount);
    }

    @Transactional
    public void updateMemberInfo(String memberEmail, MyPageUpdateDto myPageUpdateDto) {
        if (myPageUpdateDto.getNickname().length() > 7) // 문자 수 7자 초과시 예외 발생
            throw new CustomException(ErrorCode.ARGUMENT_NOT_SATISFIED);

        Member member = memberRepository.findByEmail(memberEmail).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (myPageUpdateDto.getProfilePic() != null && !myPageUpdateDto.getProfilePic().isBlank()) {
            if (member.getProfilePic() != null && member.getProfilePic().startsWith(bucketFullUrl))
                s3Service.deleteFile(member.getProfilePic()); // S3에 있는 기존 프로필 사진 삭제
            member.updateMemberProfilePic(myPageUpdateDto.getProfilePic());
            log.info("Member {} - updated profilePic", member.getEmail());
        }
        if (myPageUpdateDto.getNickname() != null && !myPageUpdateDto.getNickname().equals(member.getNickname())) {
            member.updateMemberNickname(myPageUpdateDto.getNickname());
            log.info("Member {} - updated nickname : ", member.getEmail());
        }
        if (myPageUpdateDto.getNewMainTitle() != null && !myPageUpdateDto.getNewMainTitle().equals(member.getMainTitle())) {
            MemberTitle memberTitle = memberTitleRepository.findByMemberAndTitle(member, myPageUpdateDto.getNewMainTitle());
            member.updateMemberMainTitle(memberTitle.getTitle());
            log.info("Member {} - updated mainTitle", member.getEmail());
        }
    }
}
