package com.dongyang.dongpo.repository.notice;

import com.dongyang.dongpo.domain.board.NoticePic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticePicRepository extends JpaRepository<NoticePic, Long> {

    List<NoticePic> findByNoticeId(Long noticeId);
}
