package com.dongyang.dongpo.repository.notice;

import com.dongyang.dongpo.domain.board.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
