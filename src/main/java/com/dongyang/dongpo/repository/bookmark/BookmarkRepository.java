package com.dongyang.dongpo.repository.bookmark;


import com.dongyang.dongpo.domain.member.StoreBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<StoreBookmark, Long> {
}
