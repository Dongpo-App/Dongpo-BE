package com.dongyang.dongpo.domain.member.repository;

import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.member.entity.MemberTitle;
import com.dongyang.dongpo.domain.member.entity.Title;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberTitleRepository extends JpaRepository<MemberTitle, Long> {
    List<MemberTitle> findByMember(Member member);

    MemberTitle findByMemberAndTitle(Member member, Title title);
}
