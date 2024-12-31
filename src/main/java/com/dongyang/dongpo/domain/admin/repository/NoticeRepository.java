package com.dongyang.dongpo.domain.admin.repository;

import com.dongyang.dongpo.domain.board.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
