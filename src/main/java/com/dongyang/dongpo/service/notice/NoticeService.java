package com.dongyang.dongpo.service.notice;

import com.dongyang.dongpo.domain.admin.Admin;
import com.dongyang.dongpo.dto.notice.NoticeDto;
import com.dongyang.dongpo.domain.board.Notice;
import com.dongyang.dongpo.repository.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NoticeService {

    private final NoticeRepository noticeRepository;

    public List<NoticeDto> findAll(){
        List<Notice> notices =  noticeRepository.findAll();
        List<NoticeDto> noticeDtos = new ArrayList<>();
        for (Notice notice : notices)
            noticeDtos.add(notice.toResponse());

        return noticeDtos;
    }

    @Transactional
    public void addNotice(NoticeDto noticeDto, Admin principal) {
        Notice notice = noticeRepository.save(noticeDto.toEntity(principal));

        log.info("Add Notice ID : {} by Admin ID : {}", notice.getId(), principal.getId());
    }

    public NoticeDto detail(Long id) {
        Notice notice = noticeRepository.findById(id).orElse(null);
        return notice.toResponse();
    }
}
