package com.dongyang.dongpo.domain.member.repository;

import com.dongyang.dongpo.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    Optional<Member> findBySocialId(String socialId);

    Boolean existsBySocialId(String socialId);

    Boolean existsByEmail(String email);
}
