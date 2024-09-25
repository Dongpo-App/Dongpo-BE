package com.dongyang.dongpo.service.title;

import com.dongyang.dongpo.domain.member.Member;
import com.dongyang.dongpo.domain.member.MemberTitle;
import com.dongyang.dongpo.domain.member.Title;
import com.dongyang.dongpo.dto.mypage.MyPageDto;
import com.dongyang.dongpo.repository.member.MemberTitleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TitleService {

    private final MemberTitleRepository memberTitleRepository;

    @Transactional
    public void addTitle(Member member, Title title) {
        List<MemberTitle> memberTitles = memberTitleRepository.findByMember(member);
        if (memberTitles.stream().noneMatch(t -> t.getTitle().equals(title))) {
            memberTitleRepository.save(MemberTitle.builder()
                    .title(title)
                    .achieveDate(LocalDateTime.now())
                    .member(member)
                    .build());

            log.info("member {} add title : {}", member.getId(), title.getDescription());
        }
    }

    public List<MyPageDto.TitleDto> getMemberTitles(Member member) {
        List<MyPageDto.TitleDto> titleDtos = new ArrayList<>();
        memberTitleRepository.findByMember(member).forEach(memberTitle -> {
            titleDtos.add(MyPageDto.toTitleDto(memberTitle));
        });
        return titleDtos;
    }
}
