package com.dongyang.dongpo.domain.member.service;

import com.dongyang.dongpo.domain.auth.dto.UserInfo;
import com.dongyang.dongpo.domain.member.dto.MyPageResponseDto;
import com.dongyang.dongpo.domain.member.dto.MyPageUpdateRequestDto;
import com.dongyang.dongpo.domain.member.entity.Member;

import java.util.List;

public interface MemberService {
    Member registerNewMember(UserInfo userInfo);

    List<Member> findAll();

    Member findById(Long id);

    Member findByEmail(String email);

    void setMemberStatusLeave(Member member);

    boolean validateMemberExistence(String email, String socialId);

    MyPageResponseDto getMemberInfo(Member member);

    void updateMemberInfo(Member member, MyPageUpdateRequestDto myPageUpdateRequestDto);
}
