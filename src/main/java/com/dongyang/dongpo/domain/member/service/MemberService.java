package com.dongyang.dongpo.domain.member.service;

import com.dongyang.dongpo.common.exception.CustomException;
import com.dongyang.dongpo.common.exception.ErrorCode;
import com.dongyang.dongpo.common.fileupload.s3.S3Service;
import com.dongyang.dongpo.domain.auth.dto.*;
import com.dongyang.dongpo.domain.member.dto.MyPageResponseDto;
import com.dongyang.dongpo.domain.member.dto.MyPageUpdateRequestDto;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.member.entity.MemberTitle;
import com.dongyang.dongpo.domain.member.repository.MemberRepository;
import com.dongyang.dongpo.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MemberService {

    @Value("${cloud.aws.s3.bucket-full-url}")
    private String bucketFullUrl;

    private final MemberRepository memberRepository;
    private final TitleService titleService;
    private final StoreService storeService;
    private final S3Service s3Service;

    // 회원 가입 처리
    public Member registerNewMember(UserInfo userInfo) {
        Member member = userInfo.toMemberEntity();
        memberRepository.save(member);
        titleService.addTitle(member, member.getMainTitle());
        log.info("Registered Member {} successfully - id: {}", member.getEmail(), member.getId());
        return member;
    }

    // 사용자 전체 조회
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    // id로 사용자 조회
    public Member findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    // 이메일로 사용자 조회
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    // 탈퇴 처리
    public void setMemberStatusLeave(final Member member) {
        findByEmail(member.getEmail()).setMemberStatusLeave();

        if (member.getProfilePic() != null && member.getProfilePic().startsWith(bucketFullUrl))
            s3Service.deleteFile(member.getProfilePic()); // S3에 있는 기존 프로필 사진 삭제
    }

    // 이미 가입 된 회원인지, 가입 가능한 회원인지 검증
    public boolean validateMemberExistence(String email, String socialId) {
        // 이미 가입된 회원인지 확인
        Optional<Member> existingMemberOpt = memberRepository.findBySocialId(socialId);

        // 이미 가입된 회원인 경우
        if (existingMemberOpt.isPresent()) {
            Member existingMember = existingMemberOpt.get();

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

    public MyPageResponseDto getMemberInfo(Member member) {
        return MyPageResponseDto.builder()
                .nickname(member.getNickname())
                .profilePic(member.getProfilePic())
                .mainTitle(member.getMainTitle().getDescription())
                .registerCount(storeService.getMyRegisteredStoreCount(member))
                .titleCount(titleService.getMemberTitlesCount(member))
                .build();
    }

    public void updateMemberInfo(Member member, MyPageUpdateRequestDto myPageUpdateRequestDto) {
        if (myPageUpdateRequestDto.getNickname().length() > 7) // 문자 수 7자 초과시 예외 발생
            throw new CustomException(ErrorCode.ARGUMENT_NOT_SATISFIED);

        Member existingMember = findById(member.getId());

        if (myPageUpdateRequestDto.getProfilePic() != null && !myPageUpdateRequestDto.getProfilePic().isBlank() &&
                !myPageUpdateRequestDto.getProfilePic().equals(existingMember.getProfilePic())) {
            if (existingMember.getProfilePic() != null && existingMember.getProfilePic().startsWith(bucketFullUrl)) {
                s3Service.deleteFile(existingMember.getProfilePic()); // S3에 있는 기존 프로필 사진 삭제
            }
            existingMember.updateMemberProfilePic(myPageUpdateRequestDto.getProfilePic());
            log.info("Member {} - updated profilePic", existingMember.getEmail());
        }
        if (myPageUpdateRequestDto.getNickname() != null && !myPageUpdateRequestDto.getNickname().equals(existingMember.getNickname())) {
            existingMember.updateMemberNickname(myPageUpdateRequestDto.getNickname());
            log.info("Member {} - updated nickname", existingMember.getEmail());
        }
        if (myPageUpdateRequestDto.getNewMainTitle() != null && !myPageUpdateRequestDto.getNewMainTitle().equals(existingMember.getMainTitle())) {
            MemberTitle memberTitle = titleService.findByMemberAndTitle(existingMember, myPageUpdateRequestDto.getNewMainTitle());
            existingMember.updateMemberMainTitle(memberTitle.getTitle());
            log.info("Member {} - updated mainTitle", existingMember.getEmail());
        }
    }

}
