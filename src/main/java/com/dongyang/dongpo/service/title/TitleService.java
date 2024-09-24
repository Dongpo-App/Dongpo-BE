package com.dongyang.dongpo.service.title;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.MemberTitle;
import com.dongyang.dongpo.domain.member.Title;
import com.dongyang.dongpo.repository.member.MemberRepository;
import com.dongyang.dongpo.repository.store.StoreVisitCertRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class TitleService {

    private final MemberRepository memberRepository;

    @Transactional
    public void addTitle(Member member, Title title) {
        if (member.getTitles().stream().noneMatch(t -> t.getTitle().equals(title))) {
            member.addTitle(MemberTitle.builder()
                    .title(title)
                    .achieveDate(LocalDateTime.now())
                    .member(member)
                    .build());
            memberRepository.save(member);

            log.info("member {} add title : {}", member.getId(), title.getDescription());
        }
    }
}
