package com.dongyang.dongpo.domain.member.service;

import com.dongyang.dongpo.common.exception.CustomException;
import com.dongyang.dongpo.common.exception.ErrorCode;
import com.dongyang.dongpo.common.fileupload.s3.S3Service;
import com.dongyang.dongpo.domain.auth.dto.*;
import com.dongyang.dongpo.domain.member.dto.MyPageDto;
import com.dongyang.dongpo.domain.member.dto.MyPageUpdateDto;
import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.member.entity.MemberTitle;
import com.dongyang.dongpo.domain.member.repository.MemberRepository;
import com.dongyang.dongpo.domain.member.repository.MemberTitleRepository;
import com.dongyang.dongpo.domain.store.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class MemberService {

    @Value("${cloud.aws.s3.bucket-full-url}")
    private String bucketFullUrl;

    private final MemberRepository memberRepository;
    private final MemberTitleRepository memberTitleRepository;
    private final StoreService storeService;
    private final S3Service s3Service;

    // 회원 가입 처리
    public Member registerNewMember(UserInfo userInfo) {
        Member member = Member.toEntity(userInfo);
        memberRepository.save(member);
        memberTitleRepository.save(MemberTitle.builder()
                .member(member)
                .title(member.getMainTitle())
                .achieveDate(LocalDateTime.now())
                .build());
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

    public MyPageDto getMemberInfoIndex(Member member) {
        List<MemberTitle> memberTitles = memberTitleRepository.findByMember(member);
        Long storeRegisterCount = storeService.getMyRegisteredStoreCount(member);
        return MyPageDto.toEntity(member, memberTitles, storeRegisterCount);
    }

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
