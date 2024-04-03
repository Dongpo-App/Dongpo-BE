package com.dongyang.dongpo.repository;

import com.dongyang.dongpo.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
