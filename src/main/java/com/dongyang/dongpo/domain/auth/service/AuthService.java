package com.dongyang.dongpo.domain.auth.service;


import com.dongyang.dongpo.common.auth.jwt.JwtService;
import com.dongyang.dongpo.common.exception.CustomException;
import com.dongyang.dongpo.common.exception.ErrorCode;
import com.dongyang.dongpo.domain.auth.dto.*;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.member.service.MemberService;
import io.jsonwebtoken.Claims;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final MemberService memberService;
    private final SocialPlatformService socialPlatformService;

    // 소셜 로그인 처리
    public JwtToken handleLogin(final String oauthAccessToken) {
        final UserInfo userInfo = getKakaoUserInfo(oauthAccessToken);

        // 존재하는 회원일 경우 엔티티 조회 / 존재하지 않는 회원일 경우 회원가입 진행
        Member member = memberService.validateMemberExistence(userInfo.getEmail(), userInfo.getId()) ?
                memberService.findByEmail(userInfo.getEmail()) : memberService.registerNewMember(userInfo);

        // 로그인 처리
        return jwtService.createTokenForLoginMember(member);
    }

    // 애플 소셜 로그인 처리
    public AppleLoginResponse handleAppleLogin(final AppleLoginDto appleLoginDto) {
        final Claims appleUserClaims = getAppleUserInfo(appleLoginDto);
        final String socialId = appleUserClaims.get("sub", String.class);
        final String email = appleUserClaims.get("email", String.class);

        // 한 번 탈퇴한 애플 사용자는 인증 서버로부터 이메일이 반환되지 않음
        if (StringUtils.isBlank(email))
            throw new CustomException(ErrorCode.MEMBER_ALREADY_LEFT);

        // 이미 존재하는 회원일 경우 로그인 처리
        if (memberService.validateMemberExistence(email, socialId)) {
            final Member member = memberService.findByEmail(email);
            return AppleLoginResponse.responseJwtToken(jwtService.createTokenForLoginMember(member));
        }

        // 존재하지 않는 회원인 경우 필수 클레임 정보 반환
        return AppleLoginResponse.responseClaims(appleUserClaims);
    }

    // 애플 회원 가입 수행 메소드(추가 정보 기입)
    public JwtToken continueAppleSignup(final AppleSignupContinueDto appleSignupContinueDto) {
        // 서비스 이용 약관에 동의 하지 않았을 경우 가입 불허
        if (!appleSignupContinueDto.getIsMarketingTermsAgreed())
            throw new CustomException(ErrorCode.SERVICE_TERMS_NOT_AGREED);

        // 이미 가입된 회원인지, 가입 가능한 회원인지 검증
        if (memberService.validateMemberExistence(appleSignupContinueDto.getEmail(), appleSignupContinueDto.getSocialId()))
            throw new CustomException(ErrorCode.MEMBER_ALREADY_EXISTS);

        // 회원 정보 생성
        final UserInfo userInfo = UserInfo.builder()
                .email(appleSignupContinueDto.getEmail())
                .nickname(appleSignupContinueDto.getNickname())
                .birthyear(appleSignupContinueDto.getBirthday().substring(0, 4))
                .birthday(appleSignupContinueDto.getBirthday().substring(5))
                .gender(appleSignupContinueDto.getGender())
                .provider(Member.SocialType.APPLE)
                .id(appleSignupContinueDto.getSocialId())
                .build();

        // 존재하지 않는 회원인 경우 회원가입 진행
        final Member member = memberService.registerNewMember(userInfo);
        return jwtService.createTokenForLoginMember(member);
    }

    // 카카오 로그인 콜백 처리
    public JwtToken kakaoCallback(final String accessCode) {
        return handleLogin(socialPlatformService.kakaoCallback(accessCode));
    }

    // 카카오 사용자 정보 조회
    private UserInfo getKakaoUserInfo(final String accessToken) {
        return socialPlatformService.getKakaoUserInfo(accessToken);
    }

    // 애플 사용자 정보 조회
    private Claims getAppleUserInfo(final AppleLoginDto appleLoginDto) {
        return socialPlatformService.getAppleUserInfo(appleLoginDto);
    }

    // 로그아웃
    public void logout(final Member member) {
        jwtService.expireExistingTokens(member.getEmail());
    }

    // 탈퇴
    public void withdraw(final Member member) {
        // 추후 소셜 로그인 플랫폼 추가 시 수정
        switch (member.getSocialType()) {
            case APPLE:
                handleAppleLoginWithdrawal(member);
                break;
            case KAKAO:
                handleWithdrawal(member);
                break;
            /*
            case NAVER:
                handleWithdrawal(member);
                break;
             */
            default:
                throw new CustomException(ErrorCode.MALFORMED_TOKEN);
        }
    }

    // 탈퇴 처리
    private void handleWithdrawal(final Member member) {
        logout(member);
        memberService.setMemberStatusLeave(member);
        log.info("Member {} - LEAVE", member.getEmail());
    }

    // 애플 로그인 사용자 탈퇴 처리
    private void handleAppleLoginWithdrawal(final Member member) {
        socialPlatformService.revokeAppleRefreshToken(member);
        handleWithdrawal(member);
    }

    // 토큰 Rotation
    public JwtToken reissueAccessToken(final JwtTokenReissueDto jwtTokenReissueDto) {
        Claims claims = jwtService.parseClaims(jwtTokenReissueDto.getRefreshToken());
        return jwtService.reissueAccessToken(memberService.findByEmail(claims.getIssuer()));
    }

}
