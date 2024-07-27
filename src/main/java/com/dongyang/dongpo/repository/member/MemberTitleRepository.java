package com.dongyang.dongpo.repository.member;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.MemberTitle;
import com.dongyang.dongpo.domain.member.Title;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberTitleRepository extends JpaRepository<MemberTitle, Long> {
    List<MemberTitle> findByMember(Member member);

    MemberTitle findByMemberAndTitle(Member member, Title title);
}
