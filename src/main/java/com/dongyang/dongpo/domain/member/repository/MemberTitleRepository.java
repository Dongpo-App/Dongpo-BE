package com.dongyang.dongpo.domain.member.repository;

import com.dongyang.dongpo.domain.member.entity.Member;
import com.dongyang.dongpo.domain.member.entity.MemberTitle;
import com.dongyang.dongpo.domain.member.entity.Title;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberTitleRepository extends JpaRepository<MemberTitle, Long> {
    List<MemberTitle> findByMemberOrderByIdDesc(final Member member);

    Optional<MemberTitle> findByMemberAndTitle(final Member member, final Title title);

    Long countByMember(final Member member);
}
