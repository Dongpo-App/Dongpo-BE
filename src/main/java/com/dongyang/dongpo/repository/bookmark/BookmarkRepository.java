package com.dongyang.dongpo.repository.bookmark;


import com.dongyang.dongpo.domain.member.StoreBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<StoreBookmark, Long> {
    List<StoreBookmark> findByMemberId(Long id);
}
