package com.dongyang.dongpo.admin.service;

import com.dongyang.dongpo.admin.domain.Admin;
import com.dongyang.dongpo.admin.dto.NoticeDto;
import com.dongyang.dongpo.domain.board.Notice;
import com.dongyang.dongpo.repository.notice.NoticeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class AdminNoticeService {

    private final NoticeRepository noticeRepository;

    public List<Notice> findAll(){
        return noticeRepository.findAll();
    }

    @Transactional
    public void addNotice(NoticeDto noticeDto, Admin principal) {
        Notice notice = noticeRepository.save(noticeDto.toEntity(principal));

        log.info("Add Notice ID : {} by Admin ID : {}", notice.getId(), principal.getId());
    }
}
