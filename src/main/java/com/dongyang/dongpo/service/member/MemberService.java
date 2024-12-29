package com.dongyang.dongpo.service.member;

import com.dongyang.dongpo.domain.auth.RefreshToken;
import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.MemberTitle;
import com.dongyang.dongpo.dto.auth.AppleLoginResponse;
import com.dongyang.dongpo.dto.auth.AppleSignupContinueDto;
import com.dongyang.dongpo.dto.auth.JwtToken;
import com.dongyang.dongpo.dto.auth.UserInfo;
import com.dongyang.dongpo.dto.mypage.MyPageDto;
import com.dongyang.dongpo.dto.mypage.MyPageUpdateDto;
import com.dongyang.dongpo.exception.CustomException;
import com.dongyang.dongpo.exception.ErrorCode;
import com.dongyang.dongpo.repository.auth.RefreshTokenRepository;
import com.dongyang.dongpo.repository.member.MemberRepository;
import com.dongyang.dongpo.repository.member.MemberTitleRepository;
import com.dongyang.dongpo.service.store.StoreService;
import com.dongyang.dongpo.service.token.TokenService;
import com.dongyang.dongpo.util.jwt.JwtUtil;
import com.dongyang.dongpo.util.s3.S3Service;
import io.jsonwebtoken.Claims;
import io.micrometer.common.util.StringUtils;
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
    private final JwtUtil jwtUtil;
    private final TokenService tokenService;
    private final MemberTitleRepository memberTitleRepository;
    private final StoreService storeService;
    private final S3Service s3Service;

    @Value("${cloud.aws.s3.bucket-full-url}")
    private String bucketFullUrl;

    @Transactional
    public JwtToken socialSave(UserInfo userInfo){
        Member member = Member.toEntity(userInfo);
        if (validateMemberExistence(member.getEmail(), member.getSocialId()))
            return tokenService.createTokenForLoginMember(member);

        return registerMemberAndCreateToken(member);
    }

    // 애플 로그인 수행 메소드
    public AppleLoginResponse handleAppleLogin(Claims claims) {
        final String socialId = claims.get("sub", String.class);
        final String email = claims.get("email", String.class);

        if (StringUtils.isBlank(email))
            throw new CustomException(ErrorCode.MEMBER_ALREADY_LEFT);

        // 이미 존재하는 회원인지 검증
        if (validateMemberExistence(email, socialId)) {
            Member existingMember = memberRepository.findBySocialId(socialId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
            return AppleLoginResponse.responseJwtToken(tokenService.createTokenForLoginMember(existingMember));
        }

        // 존재하지 않는 회원인 경우 필수 클레임 정보 반환
        return AppleLoginResponse.responseClaims(claims);
    }

    // 애플 회원 가입 수행 메소드(추가 정보 기입)
    @Transactional
    public JwtToken continueAppleSignup(AppleSignupContinueDto appleSignupContinueDto) {
        // 서비스 이용 약관에 동의 하지 않았을 경우 가입 불허
        if (!appleSignupContinueDto.getIsMarketingTermsAgreed())
            throw new CustomException(ErrorCode.SERVICE_TERMS_NOT_AGREED);

        // 이미 가입된 회원인지, 가입 가능한 회원인지 검증
        if (validateMemberExistence(appleSignupContinueDto.getEmail(), appleSignupContinueDto.getSocialId()))
            throw new CustomException(ErrorCode.MEMBER_ALREADY_EXISTS);

        Member member = Member.toEntity(UserInfo.builder()
                .email(appleSignupContinueDto.getEmail())
                .nickname(appleSignupContinueDto.getNickname())
                .birthyear(appleSignupContinueDto.getBirthday().substring(0, 4))
                .birthday(appleSignupContinueDto.getBirthday().substring(5))
                .gender(appleSignupContinueDto.getGender())
                .provider(Member.SocialType.APPLE)
                .id(appleSignupContinueDto.getSocialId())
                .build()
        );

        // 가입 처리 및 토큰 발급
        return registerMemberAndCreateToken(member);
    }

    // 이미 가입 된 회원인지, 가입 가능한 회원인지 검증
    public boolean validateMemberExistence(String email, String socialId) {
        // 이미 가입된 회원인 경우
        if (memberRepository.existsBySocialId(socialId)) {
            Member existingMember = memberRepository.findBySocialId(socialId)
                    .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

            // 탈퇴한 회원인 경우 (탈퇴 시 개인 정보를 모두 삭제하므로 이 검증 과정이 필수는 아님. 더블 체크를 위해 남겨둠.)
            if (existingMember.getStatus() == Member.Status.LEAVE)
                throw new CustomException(ErrorCode.MEMBER_ALREADY_LEFT);

            // 모든 조건 통과 시 로그인 처리
            return true;
        }

        // 가입 되지 않은 회원일 경우, 이메일 중복 검사
        if (memberRepository.existsByEmail(email))
            throw new CustomException(ErrorCode.MEMBER_EMAIL_DUPLICATED);

        return false;
    }

    // 회원 가입 처리 및 토큰 발급
    private JwtToken registerMemberAndCreateToken(Member member) {
        memberRepository.save(member);
        memberTitleRepository.save(MemberTitle.builder()
                .member(member)
                .title(member.getMainTitle())
                .achieveDate(LocalDateTime.now())
                .build());

        JwtToken jwtToken = jwtUtil.createToken(member.getEmail(), member.getRole());
        RefreshToken refreshToken = RefreshToken.builder()
                .email(member.getEmail())
                .refreshToken(jwtToken.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        log.info("Registered Member {} successfully - id: {}", member.getEmail(), member.getId());
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

    public void handleLogout(Member member, String authorization) {
        tokenService.expireTokens(member.getEmail(), authorization);
    }

    @Transactional
    public void handleLeave(Member member, String authorization) {
        Member existingMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        String leavingMemberEmail = existingMember.getEmail();
        handleLogout(existingMember, authorization);
        existingMember.setMemberStatusLeave();
        log.info("Member {} - LEAVE", leavingMemberEmail);
    }
}
